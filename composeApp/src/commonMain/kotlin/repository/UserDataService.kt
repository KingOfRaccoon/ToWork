package repository

import io.ktor.http.HttpHeaders
import model.Achievement
import model.AchievementWithPicture
import model.Knowledge
import model.Message
import model.Module
import model.Page
import model.Track
import model.User
import model.UserWithProgress
import util.Postman
import util.Resource

class UserDataService(private val postman: Postman) {
    private val baseUrl = "http://158.160.106.75:8081"
    private val registrationTag = "/registration"
    private val tracksTag = "/tracks"
    private val knowledgeTag = "/knowledge"
    private val achievementsTag = "/get_achievements"
    private val allAchievementsTag = "/get_all_achievements"
    private val usersWithProgressTag = "/users_with_progress"
    private val loginTag = "/login"
    private val needRegistrationTag = "/needRegistration"
    private val userForRatingTag = "/users_with_progress_with_cc"

    private fun getPageInModuleTag(idTrack: Int, numberModuleInTrack: Int) =
        "/page_in_module/$idTrack/$numberModuleInTrack"

    private fun getModulesInTrackTag(idTrack: Int) = "/module/$idTrack"

    // fullName = firstName + lastname
    suspend fun registrationUser(
        email: String,
        fullName: String,
        password: String
    ): Resource<User> {
        return postman.post(
            baseUrl,
            registrationTag,
            mapOf("name" to email, "last_name" to fullName, "password" to password)
        )
    }

    suspend fun getTracks(token: String): Resource<List<Track>> {
        return postman.get(
            baseUrl, tracksTag,
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun getModulesInTrack(idTrack: Int, token: String): Resource<List<Module>> {
        return postman.get(
            baseUrl,
            getModulesInTrackTag(idTrack),
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun getPageInModule(
        idTrack: Int,
        numberModuleInTrack: Int,
        token: String
    ): Resource<List<Page>> {
        return postman.get(
            baseUrl,
            getPageInModuleTag(idTrack, numberModuleInTrack),
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun getKnowledge(token: String): Resource<List<Knowledge>> {
        return postman.get(
            baseUrl, knowledgeTag,
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun getAchievementsUser(token: String): Resource<List<Achievement>> {
        return postman.post(
            baseUrl,
            achievementsTag,
            null,
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun getAllAchievements(token: String): Resource<List<AchievementWithPicture>> {
        return postman.post(
            baseUrl,
            allAchievementsTag,
            null,
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun getOtherUserWithProgress(token: String): Resource<List<UserWithProgress>> {
        return postman.post(
            baseUrl,
            usersWithProgressTag,
            null,
            mapOf(HttpHeaders.Authorization to token)
        )
    }

    suspend fun login(username: String, password: String): Resource<User> {
        return postman.post(
            baseUrl,
            loginTag,
            mapOf("username" to username, "password" to password)
        )
    }

    suspend fun needRegistration(email: String): Resource<Message> {
        return postman.post(
            baseUrl,
            needRegistrationTag,
            mapOf("email" to email)
        )
    }

    suspend fun getUsersForRating(token: String): Resource<List<UserWithProgress>> {
        return postman.post(
            baseUrl,
            userForRatingTag,
            mapOf(HttpHeaders.Authorization to token)
        )
    }
}