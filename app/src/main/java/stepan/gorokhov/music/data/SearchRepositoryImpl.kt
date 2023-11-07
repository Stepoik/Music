package stepan.gorokhov.music.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.FavouriteRepository
import stepan.gorokhov.domain.repositories.SearchRepository
import stepan.gorokhov.music.data.tracks.datasources.TrackService
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val trackService: TrackService, private val favouriteRepository: FavouriteRepository) :
    SearchRepository {
    private val _foundTracks = MutableStateFlow(Playlist(listOf()))
    override val foundTracks: StateFlow<Playlist> get() = _foundTracks
    private val searchRepositoryScope = CoroutineScope(Dispatchers.Main)
    private val mutex = Mutex()
    init {
        searchRepositoryScope.launch {
            favouriteRepository.favouriteTracks.collect{
                val newFoundTracks = _foundTracks.value.tracks.toMutableList()
                for (i in _foundTracks.value.tracks.indices){
                    val isLiked = it.tracks.any { it.id==newFoundTracks[i].id }
                    if (newFoundTracks[i].isLiked != isLiked){
                        newFoundTracks[i] = newFoundTracks[i].copy(isLiked = isLiked)
                    }
                }
                _foundTracks.value = Playlist(newFoundTracks)
            }
        }
    }
    override suspend fun getTracksByName(name: String) {
        mutex.withLock {
            runCatching {
                trackService.searchTrack(name)
            }
                .onSuccess {
                    _foundTracks.value = Playlist(tracks = it.trackList.map { it.toDomain() })
                }
        }
    }

}