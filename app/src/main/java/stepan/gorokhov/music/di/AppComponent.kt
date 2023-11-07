package stepan.gorokhov.music.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import stepan.gorokhov.domain.repositories.FavouriteRepository
import stepan.gorokhov.domain.repositories.SearchRepository
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.home_screen.di.HomeDeps
import stepan.gorokhov.music.ui.main_screen.MainScreenViewModel
import stepan.gorokhov.notifications.di.NotificationDeps
import stepan.gorokhov.player_screen.di.PlayerDeps
import stepan.gorokhov.search_screen.di.SearchDeps
import javax.inject.Scope


@Scope
annotation class AppScope


@Component(modules = [TrackModule::class, RetrofitModule::class, RegistrationModule::class, DatabaseModule::class, SearchModule::class, FavouriteModule::class])
@AppScope
interface AppComponent : PlayerDeps, HomeDeps, SearchDeps, NotificationDeps {
    override val repository: TrackRepository
    override val context: Context
    override val favouriteRepository: FavouriteRepository
    override val searchRepository: SearchRepository


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun mainScreenViewModel(): MainScreenViewModel
}