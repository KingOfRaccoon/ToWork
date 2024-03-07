package viewmodel

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.AnswerBot
import model.Avatar
import model.ChatMessage
import model.Knowledge
import model.LastModule
import model.Module
import model.ModuleWithPages
import model.Page
import model.Track
import model.TrackWithModules
import model.TypeUser
import model.User
import model.UserWithProgress
import repository.OnBoardingRepository
import util.Resource

class UserDataViewModel(private val onBoardingRepository: OnBoardingRepository) : ViewModel() {
    private val settings = Settings()
    private val _emailFlow = MutableStateFlow("")
    val emailFlow = _emailFlow.asStateFlow()

    private val _emailErrorFlow = MutableStateFlow<String?>(null)
    val emailErrorFlow = _emailErrorFlow.asStateFlow()

    private val _passwordFlow = MutableStateFlow("")
    val passwordFlow = _passwordFlow.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _nameFlow = MutableStateFlow("")
    val nameFlow = _nameFlow.asStateFlow()

    private val _lastnameFlow = MutableStateFlow("")
    val lastnameFlow = _lastnameFlow.asStateFlow()

    private val _typesUsersFlow = MutableStateFlow(onBoardingRepository.typesUsers)
    val typesUsersFlow = _typesUsersFlow.asStateFlow()

    private val _userImageFlow = MutableStateFlow(getUserImage())
    val userImageFlow = _userImageFlow.asStateFlow()

    private val _userFlow = MutableStateFlow<Resource<User>?>(null)
    val userFlow = _userFlow.asStateFlow()

    private val _tracksFlow = MutableStateFlow<Resource<List<Track>>>(Resource.Loading())
    val tracksFlow = _tracksFlow.asStateFlow()

    private val _modulesFlow = MutableStateFlow<Resource<List<Module>>>(Resource.Loading())
    val modulesFlow = _modulesFlow.asStateFlow()

    private val _pagesFlow = MutableStateFlow<Resource<List<Page>>>(Resource.Loading())
    val pagesFlow = _pagesFlow.asStateFlow()

    private val _knowledgeFlow = MutableStateFlow<Resource<List<Knowledge>>>(Resource.Loading())
    val knowledgeFlow = _knowledgeFlow.asStateFlow()

    private val _needRegistrationFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val needRegistrationFlow = _needRegistrationFlow.asStateFlow()

    private val _usersWithProgressFlow =
        MutableStateFlow<Resource<List<UserWithProgress>>>(Resource.Loading())
    val usersWithProgressFlow = _usersWithProgressFlow.asStateFlow()

    private val _userProgressFlow = MutableStateFlow<Resource<List<LastModule>>>(Resource.Loading())
    val userProgressFlow = _userProgressFlow.asStateFlow()

    private val _messagesFlow = MutableStateFlow<List<Resource<ChatMessage>>>(listOf())
    val messagesFlow = _messagesFlow.asStateFlow()

    val currentTrack = MutableStateFlow(-1)
    val currentModule = MutableStateFlow(-1)

    val messageText = MutableStateFlow("")

    fun setMessage(newText: String){
        messageText.update { newText }
    }

    fun sendMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            val message = messageText.value
            messageText.update { "" }
            _messagesFlow.update { it.plus(Resource.Success(ChatMessage(it.size, true, message))) }
            _messagesFlow.update { it.plus(Resource.Loading()) }
            _messagesFlow.update {
                it.filter { it !is Resource.Loading }
                    .plus(obtainRequest(onBoardingRepository.sendMessage(message), it.size))
            }
        }
    }

    private fun obtainRequest(resource: Resource<AnswerBot>, id: Int): Resource<ChatMessage> {
        return when (resource) {
            is Resource.Error -> Resource.Error(resource.message)
            is Resource.Loading -> Resource.Loading()
            is Resource.Success -> Resource.Success(ChatMessage(id, false, resource.data.result))
        }
    }

    fun setNewTrack(newCurrent: Int) {
        currentTrack.update { newCurrent }
    }

    fun setNewModule(newCurrent: Int, numberModule: Int = newCurrent) {
        currentModule.update { newCurrent }
        loadPages(currentTrack.value, numberModule)
    }

    fun getModule() = combine(
        currentModule,
        modulesFlow,
        pagesFlow,
        userProgressFlow
    ) { current, modules, pages, userProgress ->
        when (modules) {
            is Resource.Error -> Resource.Error(modules.message)
            is Resource.Loading -> Resource.Loading()
            is Resource.Success -> Resource.Success(modules.data.find { it.id == current }?.let {
                ModuleWithPages(
                    it.id,
                    it.name,
                    it.quantityPage,
                    it.quantityCoin,
                    it.startContent,
                    it.endContent,
                    it.numberInTrack,
                    it.idTrack,
                    it.completePages,
                    pages.data?.filter { it.idModule == current }.orEmpty().sortedBy { it.numberInModule }.also {
                        println("pages: $it")
                    }
                )
            })
        }
    }

    fun getTrack() = combine(
        currentTrack,
        tracksFlow,
        modulesFlow,
        userProgressFlow
    ) { current, tracks, modules, userProgress ->
        when (tracks) {
            is Resource.Error -> Resource.Error(tracks.message)
            is Resource.Loading -> Resource.Loading()
            is Resource.Success -> Resource.Success(tracks.data.find { it.id == current }?.let {
                TrackWithModules(
                    it.id,
                    it.name,
                    it.quantityModules,
                    userProgress.data?.filter { it.idTrack == current }?.map { it.idModule }
                        .orEmpty().let { if (it.isEmpty()) 0 else it.max() },
                    modules.data?.filter { it.idTrack == current }.orEmpty()
                )
            })
        }
    }


    fun registrationUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _userFlow.update { Resource.Loading() }
            _userFlow.update {
                onBoardingRepository.registrationUser(
                    emailFlow.value,
                    nameFlow.value + " " + lastnameFlow.value,
                    passwordFlow.value
                ).also {
                    if (it is Resource.Success) {
                        setToken(it.data.token)
                        loadTracksAndModules("Bearer " + it.data.token)
                        loadKnowledge("Bearer " + it.data.token)
                    }
                }
            }
        }
    }

    fun authUser(): Boolean {
        if (emailFlow.value.trim().isEmpty())
            _emailErrorFlow.update { "Укажите ваш логин" }

        if (passwordFlow.value.trim().isEmpty())
            _passwordError.update { "Укажите ваш пароль" }

        if (emailFlow.value.trim().isEmpty() || passwordFlow.value.trim().isEmpty())
            return false

        viewModelScope.launch(Dispatchers.IO) {
            _userFlow.update { Resource.Loading() }
            _userFlow.update {
                onBoardingRepository.login(emailFlow.value, passwordFlow.value).also {
                    if (it is Resource.Success) {
                        setToken(it.data.token)
                        loadTracksAndModules("Bearer " + it.data.token)
                        loadKnowledge("Bearer " + it.data.token)
                    } else if (it is Resource.Error) {
                        _emailErrorFlow.update { "Проверьте правильность ввода данных" }
                        _emailErrorFlow.update { "Проверьте правильность ввода данных" }
                    }
                }
            }
        }

        return true
    }

    fun authUserOnToken() {
        viewModelScope.launch(Dispatchers.IO) {
            getToken().let { token ->
                if (token.isNotEmpty()) {
                    _userFlow.update { Resource.Loading() }
                    _userFlow.update {
                        onBoardingRepository.authUserOnToken(token).also {
                            if (it is Resource.Success) {
                                setToken(it.data.token)
                                loadTracksAndModules("Bearer " + it.data.token)
                                loadKnowledge("Bearer " + it.data.token)
                            }
                        }
                    }
                }
            }
        }
    }

    fun loadTracksAndModules(token: String = getToken()) {
        viewModelScope.launch(Dispatchers.IO) {
            _tracksFlow.update {
                onBoardingRepository.getTracks(token).also {
                    if (it is Resource.Success)
                        it.data.forEach { track ->
                            _modulesFlow.update {
                                Resource.Success(
                                    (it.data.orEmpty() + onBoardingRepository.getModulesInTrack(
                                        track.id,
                                        token
                                    ).data.orEmpty()).distinctBy { it.id }
                                )
                            }
                        }
                }
            }
        }
    }

    fun loadKnowledge(token: String = getToken()) {
        viewModelScope.launch(Dispatchers.IO) {
            _knowledgeFlow.update { onBoardingRepository.getKnowledge(token) }
        }
    }

    fun loadUsersWithRating() {
        viewModelScope.launch(Dispatchers.IO) {
            _usersWithProgressFlow.update { onBoardingRepository.getOtherUserWithProgress(getToken()) }
        }
    }

    fun loadModules(idTrack: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _modulesFlow.update {
                Resource.Success(
                    (it.data.orEmpty() + onBoardingRepository.getModulesInTrack(
                        idTrack,
                        getToken()
                    ).data.orEmpty()).distinctBy { it.id }
                )
            }
        }
    }

    fun loadUsersProgress() {
        viewModelScope.launch(Dispatchers.IO) {
            _userProgressFlow.update {
                onBoardingRepository.getLastModules(getToken())
            }
        }
    }

    fun loadPages(idTrack: Int, idModule: Int) {
        viewModelScope.launch {
            _pagesFlow.update {
                Resource.Success(
                    (it.data.orEmpty() + onBoardingRepository.getPageInModule(
                        idTrack,
                        idModule,
                        getToken()
                    ).data.orEmpty()).distinctBy { it.id }
                )
            }
        }
    }

    fun clearRegistration() {
        _needRegistrationFlow.update { null }
    }

    fun needRegistration() {
        viewModelScope.launch(Dispatchers.IO) {
            _needRegistrationFlow.update { Resource.Loading() }
            _needRegistrationFlow.update {
                onBoardingRepository.needRegistration(emailFlow.value).let {
                    when (it) {
                        is Resource.Error -> Resource.Error("fail")
                        is Resource.Loading -> Resource.Loading()
                        is Resource.Success -> Resource.Success(it.data.message.contains("not"))
                    }
                }
            }
        }
    }

    fun getOnBoardingsArray() = onBoardingRepository.onBoardings

    fun setUserImage(avatar: Avatar) {
        _userImageFlow.update { avatar }
        settings["avatar"] = avatar.name
    }

    private fun getUserImage() =
        Avatar.entries.find { settings.getString("avatar", "") == it.name } ?: Avatar.First

    fun updateTypesUsersState(typeUser: TypeUser) {
        _typesUsersFlow.update {
            it.map { if (it.name == typeUser.name) it.copy(state = !it.state) else it }
                .toTypedArray()
        }
    }

    fun setNewEmail(newText: String) {
        _emailFlow.update { newText }
    }

    fun setNewName(newText: String) {
        _nameFlow.update { newText }
    }

    fun setNewLastname(newText: String) {
        _lastnameFlow.update { newText }
    }

    fun setNewPassword(newText: String) {
        _passwordFlow.update { newText }
    }

    fun isTokenEmpty() = getToken().replace("Bearer ", "").isEmpty()

    private fun setToken(token: String) = settings.set("token", token)
    private fun getToken() = "Bearer " + settings.getString("token", "")
}