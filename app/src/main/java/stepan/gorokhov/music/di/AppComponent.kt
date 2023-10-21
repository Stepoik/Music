package stepan.gorokhov.music.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.home_screen.di.HomeDeps
import stepan.gorokhov.music.ui.main_screen.MainScreenViewModel
import stepan.gorokhov.music.ui.search_screen.SearchScreenViewModel
import stepan.gorokhov.player_screen.di.PlayerDeps
import stepan.gorokhov.scopes.AppContext
import javax.inject.Scope


@Scope
annotation class AppScope


@Component(modules = [TrackModule::class])
@AppScope
interface AppComponent:PlayerDeps, HomeDeps{
    override val repository: TrackRepository

    @Component.Factory
    interface Factory {
        fun create(@AppContext @BindsInstance context: Context): AppComponent
    }
    fun mainScreenViewModel():MainScreenViewModel
    fun searchScreenViewModel():SearchScreenViewModel
}