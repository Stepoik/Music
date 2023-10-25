package stepan.gorokhov.search_screen.di

import dagger.Component
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.search_screen.ui.SearchScreenViewModel
import javax.inject.Scope

@Scope
annotation class SearchScope

interface SearchDeps{
    val repository: TrackRepository
}
@Component(dependencies = [SearchDeps::class])
@SearchScope
internal interface SearchComponent {
    @Component.Factory
    interface Factory{
        fun create(searchDeps: SearchDeps):SearchComponent
    }
    fun searchViewModel():SearchScreenViewModel
}