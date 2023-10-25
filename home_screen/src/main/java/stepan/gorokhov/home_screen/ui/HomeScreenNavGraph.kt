package stepan.gorokhov.home_screen.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import stepan.gorokhov.home_screen.HomeDaggerViewModel
import stepan.gorokhov.home_screen.di.HomeDeps
import stepan.gorokhov.utils.daggerViewModel

const val HOME_SCREEN_ROUTE = "home_screen"

fun NavGraphBuilder.homeScreen(homeDeps: HomeDeps, onSearchClicked:()->Unit){
    composable(HOME_SCREEN_ROUTE){
        val daggerHomeViewModel = daggerViewModel {
            HomeDaggerViewModel(homeDeps)
        }
        HomeScreen(viewModel = daggerViewModel {
            daggerHomeViewModel.homeComponent.homeScreenViewModel()
        }, onSearchClicked = onSearchClicked)
    }
}

fun NavController.navigateToHomeScreen(){
    this.navigate(HOME_SCREEN_ROUTE)
}