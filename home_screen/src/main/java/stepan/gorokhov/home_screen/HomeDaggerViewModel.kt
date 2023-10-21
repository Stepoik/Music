package stepan.gorokhov.home_screen

import androidx.lifecycle.ViewModel
import stepan.gorokhov.home_screen.di.DaggerHomeComponent
import stepan.gorokhov.home_screen.di.HomeComponent
import stepan.gorokhov.home_screen.di.HomeDeps

class HomeDaggerViewModel(deps:HomeDeps): ViewModel() {
    internal val homeComponent:HomeComponent = DaggerHomeComponent.factory().create(deps)
}