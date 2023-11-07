package stepan.gorokhov.domain.repositories

import kotlinx.coroutines.flow.StateFlow
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.ReplayState
import stepan.gorokhov.domain.models.Track

interface TrackRepository {
    val currentTrack: StateFlow<Track?>
    val trackProgress:StateFlow<Int>
    val isPlaying:StateFlow<Boolean>
    val replayState:StateFlow<ReplayState>

    fun nextReplayState()
    suspend fun play()
    suspend fun play(track: Track, playlist: Playlist)
    fun stop()
    suspend fun playNext()

    suspend fun playPrevious()
    fun rewind(rewindValue:Float)

}