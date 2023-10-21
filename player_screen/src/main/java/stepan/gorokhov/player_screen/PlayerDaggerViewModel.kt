package stepan.gorokhov.player_screen

import androidx.lifecycle.ViewModel
import stepan.gorokhov.player_screen.di.DaggerPlayerComponent
import stepan.gorokhov.player_screen.di.PlayerComponent
import stepan.gorokhov.player_screen.di.PlayerDeps
import javax.inject.Inject

class PlayerDaggerViewModel @Inject constructor(playerDeps: PlayerDeps):ViewModel() {
    internal val playerComponent:PlayerComponent = DaggerPlayerComponent.factory().create(playerDeps)
}