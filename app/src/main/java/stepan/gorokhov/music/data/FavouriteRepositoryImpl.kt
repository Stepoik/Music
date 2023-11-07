package stepan.gorokhov.music.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.FavouriteRepository
import stepan.gorokhov.music.data.favourite_tracks.FavouriteDao
import stepan.gorokhov.music.data.favourite_tracks.FavouriteTrackEntity
import stepan.gorokhov.music.mapState
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(private val favouriteDao:FavouriteDao) :FavouriteRepository {
    override val favouriteTracks: Flow<Playlist>
        get() = favouriteDao.getFavouriteTracks().map{
        Playlist(it.map { it.toDomain() })
    }
    override suspend fun likeTrack(track: Track) {
        favouriteDao.likeTrack(FavouriteTrackEntity.fromDomain(track).copy(isLiked = !track.isLiked))
    }
}