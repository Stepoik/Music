package stepan.gorokhov.music.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import stepan.gorokhov.music.ui.home_screen.HomeScreenViewModel
import stepan.gorokhov.music.ui.main_screen.MainScreenViewModel
import stepan.gorokhov.music.ui.player_screen.PlayerScreenViewModel
import stepan.gorokhov.music.ui.search_screen.SearchScreenViewModel
import javax.inject.Qualifier
import javax.inject.Scope


@Scope
annotation class AppScope

@Qualifier
annotation class AppContext

@Component(modules = [TrackModule::class])
@AppScope
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@AppContext @BindsInstance context: Context): AppComponent
    }
    fun playerScreenViewModel():PlayerScreenViewModel
    fun homeScreenViewModel():HomeScreenViewModel
    fun mainScreenViewModel():MainScreenViewModel
    fun searchScreenViewModel():SearchScreenViewModel
}