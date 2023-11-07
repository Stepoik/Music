package stepan.gorokhov.domain.repositories

interface RegistrationRepository {
    suspend fun register(email:String, password:String):Result<String>

    suspend fun login(email:String, password:String):Result<String>
}