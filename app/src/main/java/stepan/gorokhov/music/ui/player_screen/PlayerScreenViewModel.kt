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
    val replayState = trackRepository.replayState
    val track: StateFlow<Track?> get() = trackRepository.currentTrack
    val progress = trackRepository.trackProgress
    private var firstTrack: Track? = null
    private var playlist: Playlist? = null
    private val mutex = Mutex()
    fun changeReplayState() {
        trackRepository.nextReplayState()
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