package stepan.gorokhov.home_screen.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import stepan.gorokhov.home_screen.HomeDaggerViewModel
import stepan.gorokhov.utils.daggerViewModel

const val HOME_SCREEN_ROUTE = "home_screen"

fun NavGraphBuilder.homeScreen(homeDaggerViewModel: HomeDaggerViewModel, onSearchClicked:()->Unit){
    composable(HOME_SCREEN_ROUTE){
        HomeScreen(viewModel = daggerViewModel {
            homeDaggerViewModel.homeComponent.homeScreenViewModel()
        }, onSearchClicked = onSearchClicked)
    }
}

fun NavController.navigateToHomeScreen(){
    this.navigate(HOME_SCREEN_ROUTE)
}