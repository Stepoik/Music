package stepan.gorokhov.home_screen.di

import dagger.Component
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.home_screen.ui.HomeScreenViewModel
import javax.inject.Scope


@Scope
annotation class HomeScope

interface HomeDeps{
    val repository:TrackRepository
}


@HomeScope
@Component(dependencies = [HomeDeps::class])
internal interface HomeComponent {
    @Component.Factory
    interface Factory{
        fun create(deps:HomeDeps):HomeComponent
    }
    fun homeScreenViewModel():HomeScreenViewModel
}