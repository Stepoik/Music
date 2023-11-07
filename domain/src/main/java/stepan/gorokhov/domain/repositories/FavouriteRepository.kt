package stepan.gorokhov.domain.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.Track

interface FavouriteRepository {
    val favouriteTracks: Flow<Playlist>

    suspend fun likeTrack(track: Track)
}