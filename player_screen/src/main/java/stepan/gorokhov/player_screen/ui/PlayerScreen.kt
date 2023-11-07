package stepan.gorokhov.player_screen.ui

import stepan.gorokhov.player_screen.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import stepan.gorokhov.components.Minute
import stepan.gorokhov.domain.models.Artist
import stepan.gorokhov.domain.models.ReplayState
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.player_screen.PlayerDaggerViewModel
import stepan.gorokhov.utils.daggerViewModel

@Composable
fun PlayerScreen(
    daggerDepsViewModel: PlayerDaggerViewModel,
    onClose: () -> Unit,
    interactionSource: MutableInteractionSource
) {
    val viewModel = daggerViewModel {
        daggerDepsViewModel.playerComponent.playerScreenViewModel()
    }
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
        onLikeClick = { viewModel.likeTrack() },
        onValueChange = { viewModel.rewind(it) },
        interactionSource = interactionSource,
        onPlayPreviousClick = {viewModel.playPrevious()}
    )
}


@Composable
internal fun PlayerScreenContent(
    progress: Int,
    track: Track,
    replayState: ReplayState,
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit,
    onLoopClicked: () -> Unit,
    onPlayNextClick: () -> Unit,
    onClose: () -> Unit,
    onLikeClick: () -> Unit,
    onValueChange: (Float) -> Unit,
    onPlayPreviousClick:()->Unit,
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
                    .fillMaxWidth(), track = track,
                onLikeClick = onLikeClick
            )
            TrackProgressBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                track = track,
                currentSecond = progress,
                onValueChange = onValueChange
            )
            StartStopBar(
                modifier = Modifier.padding(top = 83.dp),
                isPlaying = isPlaying,
                replayState = replayState,
                onPlayClick = onPlayClick,
                onStopClick = onStopClick,
                onLoopClick = onLoopClicked,
                onPlayNextClick = onPlayNextClick,
                onPlayPreviousClick = onPlayPreviousClick
            )
        }
    }
}

@Composable
internal fun StartStopBar(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    replayState: ReplayState,
    onPlayClick: () -> Unit,
    onStopClick: () -> Unit,
    onLoopClick: () -> Unit,
    onPlayNextClick: () -> Unit,
    onPlayPreviousClick:()->Unit
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
                painterResource(id = R.drawable.previous_button),
                contentDescription = "Previous track",
                tint = Color.White,
                modifier = Modifier
                    .clickable { onPlayPreviousClick() }
                    .size(30.dp)
            )
            val playIcon =
                if (isPlaying) painterResource(id = R.drawable.stop_icon) else rememberVectorPainter(Icons.Filled.PlayArrow)
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
                    .size(30.dp)
            )
            Icon(
                painterResource(id = R.drawable.forward_button),
                contentDescription = "Next track",
                tint = Color.White,
                modifier = Modifier
                    .clickable { onPlayNextClick() }
                    .size(30.dp)
            )
        }
        ReplayIcon(onClick = onLoopClick, replayState = replayState)
    }
}

@Composable
internal fun ReplayIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    replayState: ReplayState
) {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrackProgressBar(
    modifier: Modifier = Modifier,
    track: Track,
    currentSecond: Int,
    onValueChange: (Float) -> Unit
) {
    var progress by remember {
        mutableFloatStateOf(0f)
    }
    var changing by remember {
        mutableStateOf(false)
    }
    var sliderThumbSize by remember {
        mutableIntStateOf(0)
    }
    val maxSize = 10
    val minSize = 0
    val step = 1
    LaunchedEffect(changing) {
        if (!changing) {
            while (sliderThumbSize >= minSize) {
                sliderThumbSize -= step
                delay(10)
            }
            sliderThumbSize = minSize
        } else {
            while (sliderThumbSize < maxSize) {
                sliderThumbSize += step
                delay(10)
            }
            sliderThumbSize = maxSize
        }
    }
    val sliderValue = if (changing) {
        progress
    } else {
        currentSecond.toFloat() / track.duration
    }
    Column(modifier = modifier) {
        val interactionSource = remember { MutableInteractionSource() }
        Slider(value = sliderValue,
            onValueChange = {
                changing = true
                progress = it
            },
            modifier = Modifier.fillMaxWidth(),
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    thumbSize = DpSize(sliderThumbSize.dp, sliderThumbSize.dp),
                    modifier = Modifier.padding(top = 5.dp)
                )
            },
            onValueChangeFinished = {
                changing = false
                onValueChange(progress)
            })
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
internal fun TrackNameWithArtist(
    modifier: Modifier = Modifier,
    track: Track,
    onLikeClick: () -> Unit
) {
    val artists: String = track.artists.joinToString(separator = ", ") { it.name }
    val likedIcon = if (track.isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)){
            Text(
                text = track.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artists,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            likedIcon,
            contentDescription = "Like",
            tint = Color.White,
            modifier = Modifier.clickable {
                onLikeClick()
            })
    }
}

@Composable
internal fun Header(
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
internal fun TrackCover(modifier: Modifier = Modifier, track: Track) {
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
internal fun PlayerScreenPreview() {
    MaterialTheme {
        PlayerScreenContent(
            0,
            Track(
                "id",
                "You Right",
                listOf(Artist("Doja Cat"), Artist("The Weekend")),
                isLiked = false,
                image = "https://sun1-18.userapi.com/impf/SBl28x3wXUGO5w9jRpZ4mBxRk1vAsdMpBeCkXQ/Rt-MggsKdjE.jpg?size=1920x768&quality=95&crop=0,0,1326,530&sign=72f2c86328e2e9c5e781dbb0b0435718&type=cover_group",
                url = ""
            ),
            ReplayState.NoReplay, false, {}, {}, {}, {}, {}, {}, {},{}, MutableInteractionSource(),
        )
    }
}