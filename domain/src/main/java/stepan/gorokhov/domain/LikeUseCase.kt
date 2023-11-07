package stepan.gorokhov.domain

import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.FavouriteRepository
import stepan.gorokhov.domain.repositories.TrackRepository

class LikeUseCase(
    private val favouriteRepository: FavouriteRepository,
) {
    suspend fun invoke(track: Track) {
        favouriteRepository.likeTrack(track)
    }
}