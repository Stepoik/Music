package stepan.gorokhov.music.domain.repositories

import kotlinx.coroutines.flow.StateFlow
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.ReplayState
import stepan.gorokhov.music.domain.models.Track

interface TrackRepository {
    val currentTrack: StateFlow<Track?>
    val trackProgress:StateFlow<Int>
    val isPlaying:StateFlow<Boolean>
    val replayState:StateFlow<ReplayState>

    fun nextReplayState()
    suspend fun getTracksByName(name:String):Result<Playlist>
    suspend fun getFavoritePlaylist():Result<Playlist>
    suspend fun play()
    suspend fun play(track: Track, playlist: Playlist)
    fun stop()
    suspend fun playNext()
}