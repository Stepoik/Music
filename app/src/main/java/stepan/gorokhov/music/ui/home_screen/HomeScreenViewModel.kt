package stepan.gorokhov.music.ui.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.domain.repositories.TrackRepository
import javax.inject.Inject

class HomeScreenViewModel @Inject constructor(private val repository: TrackRepository):ViewModel() {
    private val _favouritesList = MutableStateFlow(Playlist(tracks = listOf()))
    private val currentTrack = repository.currentTrack
    private val isPlaying = repository.isPlaying
    val favouritesList: StateFlow<Playlist> get() = _favouritesList
    init {
        viewModelScope.launch {
            val resp = repository.getTracksByName("morgenshtern")
            resp.onSuccess {
                _favouritesList.value = it
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