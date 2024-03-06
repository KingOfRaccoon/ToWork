package viewmodel

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Avatar
import model.TypeUser
import model.User
import repository.OnBoardingRepository
import util.Resource

class UserDataViewModel(private val onBoardingRepository: OnBoardingRepository) : ViewModel() {
    val settings = Settings()
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

    private val _userFlow = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userFlow = _userFlow.asStateFlow()

    fun registrationUser(email: String, fullName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _userFlow.update {
                onBoardingRepository.registrationUser(email, fullName, password).also {
                    if (it is Resource.Success)
                        setToken(it.data.token)
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
            _userFlow.update {
                onBoardingRepository.login(emailFlow.value, passwordFlow.value).also {
                    if (it is Resource.Success)
                        setToken(it.data.token)
                    else if (it is Resource.Error){
                        _emailErrorFlow.update { "Проверьте правильность ввода данных" }
                        _emailErrorFlow.update { "Проверьте правильность ввода данных" }
                    }
                }
            }
        }

        return true
    }

    fun authUserOnToken(){

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

    private fun setToken(token: String) = settings.set("token", token)
    private fun getToken() = settings.getString("token", "")
}