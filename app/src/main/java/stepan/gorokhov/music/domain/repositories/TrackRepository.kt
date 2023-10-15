package stepan.gorokhov.music.domain.repositories

import kotlinx.coroutines.flow.StateFlow
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track

interface TrackRepository {
    val currentTrack: StateFlow<Track?>
    val trackProgress:StateFlow<Int>
    val isPlaying:StateFlow<Boolean>
    suspend fun getTracksByName(name:String):Result<Playlist>
    suspend fun play()
    suspend fun play(track: Track, playlist: Playlist)
    fun stop()
    suspend fun playNext()
}