package stepan.gorokhov.music.ui.search_screen

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import stepan.gorokhov.music.appComponent
import stepan.gorokhov.music.utils.daggerViewModel

const val SEARCH_SCREEN_ROUTE = "search_screen"

fun NavGraphBuilder.searchScreen(){
    composable(SEARCH_SCREEN_ROUTE){
        val component = appComponent()
        SearchScreen(viewModel = daggerViewModel {
            component.searchScreenViewModel()
        })
    }
}

fun NavController.navigateToSearchScreen(){
    this.navigate(SEARCH_SCREEN_ROUTE)
}