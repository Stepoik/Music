package stepan.gorokhov.domain.repositories

import kotlinx.coroutines.flow.StateFlow
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.Track

interface SearchRepository {
    val foundTracks: StateFlow<Playlist>
    suspend fun getTracksByName(name:String)

}