package stepan.gorokhov.music.ui.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import stepan.gorokhov.music.appComponent
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.ui.components.Minute
import stepan.gorokhov.music.ui.home_screen.HomeScreen
import stepan.gorokhov.music.ui.player_screen.PlayerScreen
import stepan.gorokhov.music.ui.theme.MusicTheme
import stepan.gorokhov.music.utils.daggerViewModel


val mockTrack = Track(
    "You Right",
    listOf(Artist("Doja Cat"), Artist("The Weekend")),
    isLiked = false,
    url = "",
    image = "https://static-cdn.jtvnw.net/jtv_user_pictures/e70b3a04-a290-43e5-854f-96a495a2a330-profile_image-70x70.png"
)

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    MainScreenContent(track = viewModel.track.collectAsState().value)
}

@Composable
fun TrackBottom(track: Track, bottomOffset:Float) {
    val component = appComponent()
    Box{
        PlayerScreen(viewModel = daggerViewModel {
            component.playerScreenViewModel()
        })
        val alpha = bottomOffset.dp/LocalConfiguration.current.screenHeightDp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.alpha(alpha.value).fillMaxWidth().background(Color.Cyan).padding(horizontal = 24.dp, vertical = 2.dp)
        ) {
            Row {
                AsyncImage(
                    model = track.image,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .size(56.dp)
                )
                Column {
                    Text(text = track.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = track.artists[0].name, style = MaterialTheme.typography.labelSmall)
                }
            }
            Minute(seconds = track.duration)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenContent(track: Track?) {
    val sheetState = rememberBottomSheetScaffoldState()
    val content: @Composable () -> Unit = if (track != null) {
        {
            TrackBottom(track, sheetState.bottomSheetState.requireOffset())
        }
    } else {
        {}
    }
    var sheetPeekHeight by remember{
        mutableStateOf(0.dp)
    }
    val component = appComponent()
    BottomSheetScaffold(sheetContent = {content()},
        sheetPeekHeight = sheetPeekHeight,
        scaffoldState = sheetState
        ) {
        HomeScreen(viewModel = daggerViewModel {
            component.homeScreenViewModel()
        })
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(track) {
        if (track == null){
            sheetPeekHeight = 0.dp
        }
        else{
            sheetPeekHeight = 60.dp
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MusicTheme {
        MainScreenContent(null)
    }
}