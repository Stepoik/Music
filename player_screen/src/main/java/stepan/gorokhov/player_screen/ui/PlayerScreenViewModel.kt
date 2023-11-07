package stepan.gorokhov.player_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import stepan.gorokhov.domain.LikeUseCase
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.FavouriteRepository
import stepan.gorokhov.domain.repositories.TrackRepository
import javax.inject.Inject

internal class PlayerScreenViewModel @Inject constructor(private val trackRepository: TrackRepository, private val favouriteRepository: FavouriteRepository) :
    ViewModel() {
    val isPlaying: StateFlow<Boolean> get() = trackRepository.isPlaying
    val replayState = trackRepository.replayState
    val track: StateFlow<Track?> get() = trackRepository.currentTrack
    val progress = trackRepository.trackProgress
    fun changeReplayState() {
        trackRepository.nextReplayState()
    }
    fun likeTrack(){
        viewModelScope.launch {
            track.value?.apply {
                LikeUseCase(favouriteRepository = favouriteRepository).invoke(this)
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
    fun playPrevious(){
        viewModelScope.launch {
            trackRepository.playPrevious()
        }
    }
    fun pause() {
        trackRepository.stop()
    }
    fun rewind(rewindValue:Float){
        trackRepository.rewind(rewindValue)
    }
}