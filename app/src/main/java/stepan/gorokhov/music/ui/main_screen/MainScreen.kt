package stepan.gorokhov.music.ui.main_screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import stepan.gorokhov.music.appComponent
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.ui.components.Minute
import stepan.gorokhov.music.ui.home_screen.HOME_SCREEN_ROUTE
import stepan.gorokhov.music.ui.home_screen.HomeScreen
import stepan.gorokhov.music.ui.home_screen.homeScreen
import stepan.gorokhov.music.ui.player_screen.PlayerScreen
import stepan.gorokhov.music.ui.search_screen.navigateToSearchScreen
import stepan.gorokhov.music.ui.search_screen.searchScreen
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
fun TrackBottom(track: Track, bottomOffset: Float, onClose: () -> Unit, onClick: () -> Unit) {
    val component = appComponent()
    val interactionSource = MutableInteractionSource()
    Box {
        PlayerScreen(viewModel = daggerViewModel {
            component.playerScreenViewModel()
        }, onClose = onClose, interactionSource = interactionSource)
        val alpha = bottomOffset.dp / LocalConfiguration.current.screenHeightDp
        if (alpha.value > 0.05) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .alpha(alpha.value)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onBackground)
                    .padding(horizontal = 24.dp, vertical = 2.dp)
                    .clickable(enabled = alpha.value>0.05, onClick = onClick)
            ) {
                Row(Modifier.weight(1f)) {
                    AsyncImage(
                        model = track.image,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .size(56.dp)
                    )
                    Column {
                        Text(
                            text = track.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = track.artists[0].name,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Minute(seconds = track.duration)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreenContent(track: Track?) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetScaffoldState()
    val content: @Composable (onClose: () -> Unit) -> Unit = if (track != null) {
        {
            BackHandler(enabled = sheetState.bottomSheetState.isExpanded) {
                it()
            }
            TrackBottom(track, sheetState.bottomSheetState.requireOffset(), onClose = it,
                onClick = {
                    scope.launch {
                        sheetState.bottomSheetState.expand()
                    }
                })
        }
    } else {
        {}
    }
    var sheetPeekHeight by remember {
        mutableStateOf(0.dp)
    }
    BottomSheetScaffold(
        sheetContent = {
            content {
                scope.launch {
                    sheetState.bottomSheetState.collapse()
                }
            }
        },
        sheetPeekHeight = sheetPeekHeight,
        scaffoldState = sheetState
    ) {
        Box(Modifier.padding(it)) {
            val navController = rememberNavController()
            NavHost(navController = navController,
                startDestination = HOME_SCREEN_ROUTE,
                enterTransition = { fadeIn(animationSpec = tween(0)) },
                exitTransition = { fadeOut(animationSpec = tween(0)) }) {
                homeScreen(onSearchClicked = { navController.navigateToSearchScreen() })
                searchScreen()
            }
        }
    }
    LaunchedEffect(track) {
        sheetPeekHeight = if (track == null) {
            0.dp
        } else {
            60.dp
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