package stepan.gorokhov.music

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import stepan.gorokhov.music.ui.main_screen.MainScreen
import stepan.gorokhov.music.ui.theme.MusicTheme
import stepan.gorokhov.notifications.ui.Notifications
import stepan.gorokhov.notifications.di.DaggerNotificationComponent
import stepan.gorokhov.utils.daggerViewModel

class MainActivity : ComponentActivity() {
    private val notifications: Notifications by lazy {
        Notifications(
            DaggerNotificationComponent.factory().create((applicationContext as App).appComponent)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notifications
        startService(Intent(this, MusicService::class.java))
        setContent {
            MusicTheme {
                Surface(
                    Modifier.padding(
                        bottom = WindowInsets.navigationBars.only(WindowInsetsSides.Bottom)
                            .asPaddingValues().calculateBottomPadding()
                    )
                ) {
                    val component = appComponent()
                    MainScreen(viewModel = daggerViewModel {
                        component.mainScreenViewModel()
                    })
                }
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicTheme {
        Greeting("Android")
    }
}