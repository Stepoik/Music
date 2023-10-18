package stepan.gorokhov.music.ui.player_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import stepan.gorokhov.music.R
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.ReplayState
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.ui.components.Minute
import stepan.gorokhov.music.ui.theme.MusicTheme
import kotlin.math.max

@Composable
fun PlayerScreen(
    viewModel: PlayerScreenViewModel,
    onClose: () -> Unit,
    interactionSource: MutableInteractionSource
) {
    val value = viewModel.progress.collectAsState().value
    PlayerScreenContent(
        progress = value,
        replayState = viewModel.replayState.collectAsState().value,
        track = viewModel.track.collectAsState().value!!,
        isPlaying = viewModel.isPlaying.collectAsState().value,
        onPlayClick = { viewModel.play() },
        onStopClick = { viewModel.pause() },
        onLoopClicked = { viewModel.changeReplayState() },
        onPlayNextClick = { viewModel.playNext() },
        onClose = onClose,
        interactionSource = interactionSource
    )
}


@Composable
fun PlayerScreenContent(
    progress: Int,
    track: Track,
    replayState: ReplayState,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit,
    onLoopClicked: () -> Unit,
    onPlayNextClick: () -> Unit,
    onClose: () -> Unit,
    interactionSource: MutableInteractionSource
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.colorScheme.background
        ),
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 100f)
    )
    Scaffold(contentWindowInsets = WindowInsets(0.dp)) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
                .background(gradient)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                modifier = Modifier.statusBarsPadding(),
                onClose = onClose,
                interactionSource = interactionSource
            )
            TrackCover(
                Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(0.8f),
                track = track
            )
            TrackNameWithArtist(
                modifier = Modifier
                    .padding(top = 39.dp)
                    .fillMaxWidth(), track = track
            )
            TrackProgressBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                track = track,
                currentSecond = progress
            )
            StartStopBar(
                modifier = Modifier.padding(top = 83.dp),
                isPlaying = isPlaying,
                replayState = replayState,
                onPlayClick = onPlayClick,
                onStopClick = onStopClick,
                onLoopClick = onLoopClicked,
                onPlayNextClick = onPlayNextClick
            )
        }
    }
}

@Composable
fun StartStopBar(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    replayState: ReplayState,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit,
    onLoopClick: () -> Unit,
    onPlayNextClick: () -> Unit
) {
    val gradient = Brush.horizontalGradient(listOf(Color(0xFF842ED8), Color(0xFFDB28A9)))
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.Clear, contentDescription = null, tint = Color.White)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Previous track",
                tint = Color.White
            )
            val playIcon =
                if (isPlaying) ImageVector.vectorResource(id = R.drawable.stop_icon) else Icons.Filled.PlayArrow
            Icon(
                playIcon,
                contentDescription = "Play track",
                tint = Color.White,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        if (isPlaying) {
                            onStopClick()
                        } else {
                            onPlayClick()
                        }
                    }
                    .background(
                        gradient
                    )
                    .padding(18.dp)
            )
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "Next track",
                tint = Color.White,
                modifier = Modifier.clickable { onPlayNextClick() }
            )
        }
        ReplayIcon(onClick = onLoopClick, replayState = replayState)
    }
}

@Composable
fun ReplayIcon(modifier: Modifier = Modifier, onClick: () -> Unit, replayState: ReplayState) {
    var color = Color.White
    val icon = when (replayState) {
        ReplayState.NoReplay -> {
            color = Color.White
            R.drawable.replay
        }

        ReplayState.ReplayPlaylist -> {
            color = Color.Cyan
            R.drawable.replay
        }

        ReplayState.ReplayTrack -> {
            color = Color.Cyan
            R.drawable.replay_current
        }
    }
    Icon(
        painterResource(id = icon),
        contentDescription = null,
        tint = color,
        modifier = modifier.clickable {
            onClick()
        })
}


@Composable
fun TrackProgressBar(modifier: Modifier = Modifier, track: Track, currentSecond: Int) {
    Column(modifier = modifier) {
        Box(Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFA5A5A5))
                    .height(4.dp)
            ) {

            }
            Column(
                Modifier
                    .fillMaxWidth(currentSecond.toFloat() / max(track.duration, 1))
                    .background(Color.White)
                    .height(4.dp)
            ) {

            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Minute(seconds = currentSecond)
            Minute(seconds = track.duration)
        }
    }
}

@Composable
fun TrackNameWithArtist(modifier: Modifier = Modifier, track: Track) {
    val artists: String = track.artists.joinToString(separator = ", ") { it.name }
    val likedIcon = if (track.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(text = track.name, style = MaterialTheme.typography.titleLarge)
            Text(text = artists, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(likedIcon, contentDescription = "Like", tint = Color.White)
    }
}

@Composable
fun Header(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource
) {
    Row(
        modifier
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            Icons.Filled.KeyboardArrowLeft,
            contentDescription = "Back",
            tint = Color.White,
            modifier = Modifier.clickable { onClose() })
        Icon(Icons.Filled.MoreVert, contentDescription = "", tint = Color.White)
    }
}

@Composable
fun TrackCover(modifier: Modifier = Modifier, track: Track) {
    Column(modifier) {
        Box(
            Modifier
                .aspectRatio(1f)
        ) {
            AsyncImage(
                track.image,
                contentDescription = "Track cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
fun PlayerScreenPreview() {
    MusicTheme {
        PlayerScreenContent(
            0,
            Track(
                "You Right",
                listOf(Artist("Doja Cat"), Artist("The Weekend")),
                isLiked = false,
                image = "https://sun1-18.userapi.com/impf/SBl28x3wXUGO5w9jRpZ4mBxRk1vAsdMpBeCkXQ/Rt-MggsKdjE.jpg?size=1920x768&quality=95&crop=0,0,1326,530&sign=72f2c86328e2e9c5e781dbb0b0435718&type=cover_group",
                url = ""
            ),
            ReplayState.NoReplay, false, {}, {}, {}, {}, {}, MutableInteractionSource(),
        )
    }
}