package stepan.gorokhov.music.data.registration.datasources

import retrofit2.http.Body
import retrofit2.http.POST
import stepan.gorokhov.music.data.registration.models.AuthKey
import stepan.gorokhov.music.data.registration.models.LoginForm
import stepan.gorokhov.music.data.registration.models.RegistrationForm

interface RegistrationService {
    @POST("/registration")
    suspend fun registration(@Body registrationForm: RegistrationForm):AuthKey

    @POST("/login")
    suspend fun login(@Body loginForm: LoginForm):AuthKey
}