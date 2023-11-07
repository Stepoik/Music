package stepan.gorokhov.music.data

import stepan.gorokhov.domain.repositories.RegistrationRepository
import stepan.gorokhov.music.data.registration.datasources.RegistrationService
import stepan.gorokhov.music.data.registration.models.LoginForm
import stepan.gorokhov.music.data.registration.models.RegistrationForm
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(private val registrationService: RegistrationService):RegistrationRepository {
    override suspend fun register(email: String, password: String):Result<String> {
        val form = RegistrationForm(login = email, password = password)
        registrationService.registration(form)
        return Result.failure(Throwable("Failed registration"))
    }

    override suspend fun login(email: String, password: String):Result<String> {
        val form = LoginForm(login = email, password = password)
        registrationService.login(form)
        return Result.failure(Throwable("Failed login"))
    }
}