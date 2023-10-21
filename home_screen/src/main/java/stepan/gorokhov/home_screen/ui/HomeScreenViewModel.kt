package stepan.gorokhov.home_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.TrackRepository
import javax.inject.Inject

internal sealed class HomeScreenState{
    object Loading: HomeScreenState()
    data class LoadedTracks(val playlist: Playlist): HomeScreenState()
}

internal class HomeScreenViewModel @Inject constructor(private val repository: TrackRepository):ViewModel() {
    private val currentTrack = repository.currentTrack
    private val isPlaying = repository.isPlaying
    private val _screenState:MutableStateFlow<HomeScreenState> = MutableStateFlow(HomeScreenState.Loading)
    val screenState:StateFlow<HomeScreenState> get() = _screenState

    init {
        update()
    }
    fun update(){
        _screenState.value = HomeScreenState.Loading
        viewModelScope.launch {
            val favouritePlaylist = repository.getFavoritePlaylist()
            favouritePlaylist.onSuccess {
                _screenState.value = HomeScreenState.LoadedTracks(it)
            }
        }
    }
    fun selectTrack(track: Track, playlist: Playlist){
        if (currentTrack.value == track && isPlaying.value){
            repository.stop()
        }
        else if (currentTrack.value == track){
            viewModelScope.launch {
                repository.play()
            }
        }
        else{
            play(track, playlist)
        }
    }
    private fun play(track: Track, playlist: Playlist){
        viewModelScope.launch {
            repository.play(track, playlist)
        }
    }
}