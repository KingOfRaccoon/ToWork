package repository

import androidx.compose.ui.text.buildAnnotatedString
import model.Message
import model.OnBoarding
import model.Track
import model.TypeUser

class OnBoardingRepository(private val userDataService: UserDataService) {
    private val bullet = "\u2022"

    val onBoardings = arrayOf(
        OnBoarding(
            listOf(
                buildAnnotatedString { append("Привет, друг!  Меня зовут Никита :)") },
                buildAnnotatedString { append("Раньше я помогал новобранцам, отвечал на множество вопросов, водил за ручку по кабинетам... НО!") }
            ), "images/onboarding_1.png"
        ),
        OnBoarding(
            listOf(buildAnnotatedString { append("Теперь есть ToWork") }),
            "images/onboarding_2.png"
        ),
        OnBoarding(listOf(buildAnnotatedString {
            append("Я помогу тебе адаптироваться на новом месте::\n")
            append(bullet)
            append("\t\t")
            append("быстро пройдем все этапы трудоустройства\n")
            append(bullet)
            append("\t\t")
            append("расскажу, как ориентироваться в корпусах\n")
            append(bullet)
            append("\t\t")
            append("познакомлю с коллегами и корпоративной культурой")
        }), "images/onboarding_3.png"),
        OnBoarding(
            listOf(
                buildAnnotatedString {
                    append(
                        "За прохождение этапов адаптации ты будешь получать Монеты, которые можешь обменять на МЕЕЕЕЕЕРЧ!\n" +
                                "Больше Мерча богу Мерча!"
                    )
                }
            ),
            "images/onboarding_4.png"
        ),
        OnBoarding(
            listOf(
                buildAnnotatedString {
                    append(
                        "Помни, я тут, в твоем кармане (сердечке) " +
                                "и всегда помогу тебе на этом нелегком пути"
                    )
                },
                buildAnnotatedString { append("Погнали!") }), "images/onboarding_5.png"
        )
    )

    val typesUsers = arrayOf(
        TypeUser("Карьерный Карась", "Получил оффер, начинаю устраиваться", true),
        TypeUser("Фрешмен", "Фух, устроился! Пора вливаться в коллектив"),
        TypeUser("Легендарный Профи", "Мм? Интересно же, что тут у вас"),
    )

    suspend fun registrationUser(email: String, fullName: String, password: String) =
        userDataService.registrationUser(email, fullName, password)

    suspend fun getTracks(token: String) =
        userDataService.getTracks(token)

    suspend fun getAchievementsUser(token: String) =
        userDataService.getAchievementsUser(token)

    suspend fun getAllAchievements(token: String) =
        userDataService.getAllAchievements(token)

    suspend fun needRegistration(email: String) =
        userDataService.needRegistration(email)

    suspend fun getOtherUserWithProgress(token: String) =
        userDataService.getOtherUserWithProgress(token)

    suspend fun login(email: String, password: String) =
        userDataService.login(email, password)

    suspend fun getKnowledge(token: String) =
        userDataService.getKnowledge(token)

    suspend fun getModulesInTrack(idTrack: Int, token: String) =
        userDataService.getModulesInTrack(idTrack, token)

    suspend fun getPageInModule(idTrack: Int, numberModuleInTrack: Int, token: String) =
        userDataService.getPageInModule(idTrack, numberModuleInTrack, token)

    suspend fun getUsersForRating(token: String) =
        userDataService.getUsersForRating(token)

    suspend fun authUserOnToken(token: String) =
        userDataService.loginWithToken(token)

    suspend fun getLastModules(token: String) =
        userDataService.getLastModules(token)

    suspend fun updateCompletePage(token: String, idModule: Int) =
        userDataService.updateCompletePage(token, idModule)

    suspend fun sendMessage(message: String) =
        userDataService.sendMessage(message)
}