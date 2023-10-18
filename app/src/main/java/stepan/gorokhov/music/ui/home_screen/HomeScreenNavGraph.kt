package stepan.gorokhov.music.ui.home_screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import stepan.gorokhov.music.appComponent
import stepan.gorokhov.music.ui.search_screen.SEARCH_SCREEN_ROUTE
import stepan.gorokhov.music.utils.daggerViewModel

const val HOME_SCREEN_ROUTE = "home_screen"

fun NavGraphBuilder.homeScreen(onSearchClicked:()->Unit){
    composable(HOME_SCREEN_ROUTE){
        val component = appComponent()
        HomeScreen(viewModel = daggerViewModel {
            component.homeScreenViewModel()
        }, onSearchClicked = onSearchClicked)
    }
}

fun NavController.navigateToHomeScreen(){
    this.navigate(HOME_SCREEN_ROUTE)
}