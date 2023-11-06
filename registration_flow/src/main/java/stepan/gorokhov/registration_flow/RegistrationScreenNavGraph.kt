package stepan.gorokhov.registration_flow

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import stepan.gorokhov.registration_flow.di.RegistrationDeps
import stepan.gorokhov.registration_flow.registration_screen.RegistrationScreen
import stepan.gorokhov.utils.daggerViewModel


const val REGISTRATION_ROUTE_PATTERN = "registration"

fun NavGraphBuilder.registrationScreen(registrationDeps: RegistrationDeps, onLogged:()->Unit){
    composable(REGISTRATION_ROUTE_PATTERN){
        val daggerVM= daggerViewModel {
            RegistrationDaggerViewModel(registrationDeps)
        }
        RegistrationScreen(onLogged = onLogged, viewModel = daggerViewModel {
            daggerVM.registrationComponent.provideRegistrationScreenViewModel()
        })
    }
}

fun NavController.navigateToRegistration(){
    navigate(REGISTRATION_ROUTE_PATTERN)
}