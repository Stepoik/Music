package stepan.gorokhov.music.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import stepan.gorokhov.domain.repositories.RegistrationRepository
import stepan.gorokhov.music.data.RegistrationRepositoryImpl
import stepan.gorokhov.music.data.registration.datasources.RegistrationService


@Module
interface RegistrationModule {
    @AppScope
    @Binds
    fun provideRegistrationRepository(registrationRepositoryImpl: RegistrationRepositoryImpl):RegistrationRepository
    companion object{
        @Provides
        fun provideTrackService(retrofit: Retrofit): RegistrationService {
            return retrofit.create(RegistrationService::class.java)
        }
    }
}