package stepan.gorokhov.player_screen.di

import dagger.Component
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.player_screen.ui.PlayerScreenViewModel
import javax.inject.Scope


interface PlayerDeps{
    val repository:TrackRepository
}
@Scope
annotation class PlayerScope

@Component(dependencies = [PlayerDeps::class])
@PlayerScope
internal interface PlayerComponent {
    @Component.Factory
    interface Factory{
        fun create(deps:PlayerDeps):PlayerComponent
    }
    fun playerScreenViewModel():PlayerScreenViewModel
}