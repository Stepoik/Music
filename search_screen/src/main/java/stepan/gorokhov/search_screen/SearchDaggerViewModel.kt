package stepan.gorokhov.search_screen

import androidx.lifecycle.ViewModel
import stepan.gorokhov.search_screen.di.DaggerSearchComponent
import stepan.gorokhov.search_screen.di.SearchComponent
import stepan.gorokhov.search_screen.di.SearchDeps

internal class SearchDaggerViewModel(searchDeps: SearchDeps): ViewModel() {
    internal val searchComponent:SearchComponent = DaggerSearchComponent.factory().create(searchDeps)
}