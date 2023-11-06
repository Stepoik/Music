package stepan.gorokhov.registration_flow

import androidx.lifecycle.ViewModel
import stepan.gorokhov.registration_flow.di.DaggerRegistrationComponent
import stepan.gorokhov.registration_flow.di.RegistrationComponent
import stepan.gorokhov.registration_flow.di.RegistrationDeps

internal class RegistrationDaggerViewModel(registrationDeps: RegistrationDeps):ViewModel() {
    val registrationComponent:RegistrationComponent = DaggerRegistrationComponent.factory().create(registrationDeps)
}