package stepan.gorokhov.search_screen.ui

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import stepan.gorokhov.search_screen.SearchDaggerViewModel
import stepan.gorokhov.search_screen.di.SearchDeps
import stepan.gorokhov.utils.daggerViewModel

const val SEARCH_SCREEN_ROUTE = "search_screen"

fun NavGraphBuilder.searchScreen(searchDeps: SearchDeps){
    composable(SEARCH_SCREEN_ROUTE){
        val viewModel = daggerViewModel {
            SearchDaggerViewModel(searchDeps)
        }
        val component = viewModel.searchComponent
        SearchScreen(viewModel = daggerViewModel {
            component.searchViewModel()
        })
    }
}

fun NavController.navigateToSearchScreen(){
    this.navigate(SEARCH_SCREEN_ROUTE)
}