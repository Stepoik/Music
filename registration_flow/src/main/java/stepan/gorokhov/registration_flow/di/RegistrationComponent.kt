package stepan.gorokhov.registration_flow.di

import dagger.Component
import stepan.gorokhov.domain.repositories.RegistrationRepository
import stepan.gorokhov.registration_flow.registration_screen.RegistrationScreenViewModel
import javax.inject.Scope

interface RegistrationDeps{
    val repository:RegistrationRepository
}
@Scope
annotation class RegistrationScope
@Component(dependencies = [RegistrationDeps::class])
@RegistrationScope
interface RegistrationComponent {
    @Component.Factory
    interface Factory{
        fun create(registrationDeps: RegistrationDeps):RegistrationComponent
    }
    fun provideRegistrationScreenViewModel(): RegistrationScreenViewModel
}