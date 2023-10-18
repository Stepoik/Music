package stepan.gorokhov.music.ui.search_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.ui.components.LoadingScreen
import stepan.gorokhov.music.ui.components.SearchTextField
import stepan.gorokhov.music.ui.components.TrackItem
import stepan.gorokhov.music.ui.main_screen.mockTrack
import stepan.gorokhov.music.ui.theme.MusicTheme

@Composable
fun SearchScreen(viewModel: SearchScreenViewModel) {
    SearchScreenContent(viewModel.searchValue.collectAsState().value, {
        viewModel.search(it)
    }, viewModel.searchState.collectAsState().value, onSelectTrack = { track, playlist ->
        viewModel.selectTrack(track = track, playlist = playlist)
    })
}

@Composable
fun SearchScreenContent(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    searchState: SearchState,
    onSelectTrack: (Track, Playlist) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Scaffold(contentWindowInsets = WindowInsets(0.dp)) {
        Column(
            Modifier
                .padding(it)
                .padding(horizontal = 24.dp)
                .fillMaxSize()
        ) {

            SearchTextField(
                value = textFieldValue,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .statusBarsPadding()
                    .padding(vertical = 20.dp)
            )
            when (searchState) {
                is SearchState.Loading -> {
                    LoadingScreen(Modifier.fillMaxSize().weight(1f))
                }

                is SearchState.SearchResult -> {
                    LazyColumn(Modifier.weight(1f)) {
                        items(searchState.playlist.tracks) { track ->
                            TrackItem(track = track, onSelect = {
                                onSelectTrack(track, searchState.playlist)
                            }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.padding(top = 10.dp))
                        }
                    }
                }
            }
        }
    }
    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }
}

@Composable
@Preview
fun SearchScreenPreview() {
    MusicTheme {
        SearchScreenContent("", {}, SearchState.SearchResult(Playlist(listOf(
            mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,
            mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,mockTrack,
        ))), { track, playlist -> })
    }
}