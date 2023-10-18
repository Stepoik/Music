package stepan.gorokhov.music.ui.home_screen

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import stepan.gorokhov.music.R
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.ui.components.LoadingScreen
import stepan.gorokhov.music.ui.components.Minute
import stepan.gorokhov.music.ui.components.SearchTextField
import stepan.gorokhov.music.ui.components.TrackItem
import stepan.gorokhov.music.ui.theme.MusicTheme

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, onSearchClicked: () -> Unit) {
    HomeScreenContent(
        viewModel.screenState.collectAsState().value,
        onSelectTrack = { track, playlist ->
            viewModel.selectTrack(track, playlist)
        },
        onSearchClicked = onSearchClicked,
        onRefresh = {
            viewModel.update()
        }
    )
}

@Composable
fun HomeScreenContent(
    screenState: HomeScreenState,
    onSelectTrack: (Track, Playlist) -> Unit,
    onSearchClicked: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(contentWindowInsets = WindowInsets(0.dp)) {
        val scrollState = rememberScrollState()
        val onBackgroundColor = MaterialTheme.colorScheme.onBackground
        var loadedFirstTime by remember {
            mutableStateOf(false)
        }
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = screenState is HomeScreenState.Loading && loadedFirstTime),
            onRefresh = onRefresh,
            indicator = { s, trigger ->
                SwipeRefreshIndicator(
                    s,
                    trigger,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .drawCircle(
                        Offset(400f, 1200f),
                        700f,
                        color = onBackgroundColor,
                        blurRadius = 800f,
                        alpha = 0.5f
                    )
                    .statusBarsPadding()
                    .verticalScroll(scrollState)
            ) {
                WelcomeText(modifier = Modifier.padding(start = 24.dp))
                SearchTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                        .clickable { onSearchClicked() },
                    enabled = false
                )
                when (screenState) {
                    is HomeScreenState.Loading -> {
                        LoadingScreen(
                            Modifier
                                .fillMaxSize()
                                .weight(1f))
                    }

                    is HomeScreenState.LoadedTracks -> {
                        loadedFirstTime = true
                        WelcomeScreen(
                            favourite = screenState.playlist,
                            onSelectTrack = onSelectTrack
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun WelcomeScreen(
    favourite: Playlist,
    onSelectTrack: (Track, Playlist) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Genres(Modifier.padding(start = 24.dp, top = 40.dp))
        PlaylistList(Modifier.padding(top = 24.dp))
        Favourites(
            modifier = Modifier.padding(top = 40.dp, start = 24.dp, end = 24.dp),
            favourite = favourite,
            onSelectTrack = onSelectTrack
        )
    }
}

@Composable
fun Favourites(
    modifier: Modifier = Modifier,
    favourite: Playlist,
    onSelectTrack: (Track, Playlist) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = "Your favourites",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 30.dp)
        )
        for (track in favourite.tracks) {
            TrackItem(track = track, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp), onSelect = {
                onSelectTrack(it, favourite)
            })
        }
    }
}

@Composable
fun PlaylistItem(artist: Artist, playlistName: String) {
    Column {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Text(
            text = playlistName,
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = artist.name,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun PlaylistList(modifier: Modifier = Modifier) {
    LazyRow(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(24.dp)) {
        item {

        }
        items(count = 5) {
            PlaylistItem(Artist("Chill your mind"), playlistName = "R&B Playlist")
        }
    }
}

@Composable
fun Genres(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = "Recent")
    }
}


@Composable
fun WelcomeText(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(text = "Welcome back!", style = MaterialTheme.typography.titleLarge)
        Text(text = "What do you feel like today?", style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    MusicTheme {
        HomeScreenContent(
            HomeScreenState.Loading, { track, playlist -> }, {}, {}
        )
    }
}

fun Modifier.drawCircle(
    offset: Offset,
    radius: Float,
    color: Color,
    blurRadius: Float,
    alpha: Float = 0.5f
) =
    drawWithContent {
        val rect = Rect(Offset.Zero, size)
        val paint = Paint()
        paint.color = color
        println(paint.color)
        drawIntoCanvas { canvas ->
            val frameworkPaint = paint.asFrameworkPaint()
            paint.color = color
            paint.alpha = alpha
            frameworkPaint.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
            canvas.drawCircle(
                center = offset,
                radius = radius,
                paint = paint
            )
            paint.color = Color.Black
            canvas.saveLayer(rect, paint = paint)
            frameworkPaint.xfermode = null
            frameworkPaint.maskFilter = null
        }
        drawContent()
    }