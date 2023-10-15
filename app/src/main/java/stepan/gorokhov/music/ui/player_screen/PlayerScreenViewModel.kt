package stepan.gorokhov.music.ui.player_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.domain.repositories.TrackRepository
import stepan.gorokhov.music.utils.MusicPlayer
import javax.inject.Inject

class PlayerScreenViewModel @Inject constructor(private val trackRepository: TrackRepository) :
    ViewModel() {
    val isPlaying: StateFlow<Boolean> get() = trackRepository.isPlaying
    val track: StateFlow<Track?> get() = trackRepository.currentTrack
    val progress = trackRepository.trackProgress
    private var firstTrack: Track? = null
    private var playlist: Playlist? = null
    private val mutex = Mutex()
    fun getFirstTrack() {
        viewModelScope.launch {
            val playlist = trackRepository.getTracksByName("morgenshtern")
            playlist.onSuccess {
                mutex.withLock {
                    if (it.tracks.isNotEmpty()) {
                        firstTrack = it.tracks[0]
                        this@PlayerScreenViewModel.playlist = it
                    }
                }
            }
        }
    }

    fun play() {
        viewModelScope.launch {
            trackRepository.play()
        }
    }
    fun playNext(){
        viewModelScope.launch {
            trackRepository.playNext()
        }
    }
    fun pause() {
        trackRepository.stop()
    }
}