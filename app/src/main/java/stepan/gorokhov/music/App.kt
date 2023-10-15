package stepan.gorokhov.music

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import stepan.gorokhov.music.di.AppComponent
import stepan.gorokhov.music.di.DaggerAppComponent

class App:Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(this)
    }
}
@Composable
fun appComponent():AppComponent{
    return (LocalContext.current.applicationContext as App).appComponent
}
