package stepan.gorokhov.search_screen.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import stepan.gorokhov.components.LoadingScreen
import stepan.gorokhov.components.SearchTextField


val mockTrack = stepan.gorokhov.domain.models.Track(
    "You Right",
    listOf(
        stepan.gorokhov.domain.models.Artist("Doja Cat"),
        stepan.gorokhov.domain.models.Artist("The Weekend")
    ),
    isLiked = false,
    url = "",
    image = "https://static-cdn.jtvnw.net/jtv_user_pictures/e70b3a04-a290-43e5-854f-96a495a2a330-profile_image-70x70.png"
)

@Composable
internal fun SearchScreen(viewModel: SearchScreenViewModel) {
    SearchScreenContent(viewModel.searchValue.collectAsState().value, {
        viewModel.search(it)
    }, viewModel.searchState.collectAsState().value, onSelectTrack = { track, playlist ->
        viewModel.selectTrack(track = track, playlist = playlist)
    })
}

@Composable
internal fun SearchScreenContent(
    textFieldValue: String,
    onValueChange: (String) -> Unit,
    searchState: SearchState,
    onSelectTrack: (stepan.gorokhov.domain.models.Track, stepan.gorokhov.domain.models.Playlist) -> Unit
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
                    LoadingScreen(
                        Modifier
                            .fillMaxSize()
                            .weight(1f))
                }

                is SearchState.SearchResult -> {
                    LazyColumn(Modifier.weight(1f)) {
                        items(searchState.playlist.tracks) { track ->
                            stepan.gorokhov.components.TrackItem(track = track, onSelect = {
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
internal fun SearchScreenPreview() {
    MaterialTheme {
        SearchScreenContent("", {}, SearchState.SearchResult(
            stepan.gorokhov.domain.models.Playlist(
                listOf(
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                    mockTrack,
                )
            )
        ), { track, playlist -> })
    }
}