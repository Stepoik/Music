package stepan.gorokhov.music.data

import android.util.Log
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
import stepan.gorokhov.music.data.tracks.datasources.TrackService
import stepan.gorokhov.utils.MusicPlayer
import javax.inject.Inject
import stepan.gorokhov.domain.repositories.TrackRepository
import kotlin.math.max

class TrackRepositoryImpl @Inject constructor(
    private val favouriteRepository: FavouriteRepository,
    private val musicPlayer: MusicPlayer
) :
    TrackRepository {
    private val _replayState = MutableStateFlow(stepan.gorokhov.domain.models.ReplayState.NoReplay)
    override val replayState: StateFlow<stepan.gorokhov.domain.models.ReplayState>
        get() = _replayState

    override val isPlaying: StateFlow<Boolean>
        get() = musicPlayer.isPlaying
    override val currentTrack: StateFlow<Track?> get() = musicPlayer.track
    override val trackProgress: StateFlow<Int> get() = musicPlayer.progress
    private var currentPlaylist: Playlist? = null
    private val repositoryScope = CoroutineScope(Dispatchers.Main)
    private val mutex = Mutex()
    private var favouriteTracks: Playlist = Playlist(listOf())

    init {
        musicPlayer.setOnCompletionListener {
            repositoryScope.launch {
                playNext()
            }
        }
        repositoryScope.launch {
            favouriteRepository.favouriteTracks.collect { playlist ->
                mutex.withLock {
                    favouriteTracks = playlist
                    var liked = false
                    for (track in playlist.tracks) {
                        if (track.id == currentTrack.value?.id) {
                            musicPlayer.likeTrack()
                            liked = true
                            break
                        }
                    }
                    if (!liked){
                        musicPlayer.dislikeTrack()
                    }
                    currentPlaylist?.tracks?.apply {
                        val updatedPlaylist = currentPlaylist!!.tracks.toMutableList()
                        for (i in currentPlaylist!!.tracks.indices) {
                            val isLiked =
                                playlist.tracks.any { it.id == updatedPlaylist[i].id && it.isLiked }
                            updatedPlaylist[i] = updatedPlaylist[i].copy(isLiked = isLiked)
                        }
                        currentPlaylist = Playlist(updatedPlaylist)
                    }
                }
            }
        }
    }

    override fun nextReplayState() {
        _replayState.value = _replayState.value.next()
    }


    override suspend fun play() {
        musicPlayer.play()
    }

    override suspend fun play(track: Track, playlist: Playlist) {
        mutex.withLock {
            val newPlaylist = playlist.tracks.toMutableList()
            for (i in newPlaylist.indices) {
                val isLiked =
                    favouriteTracks.tracks.any { it.id == newPlaylist[i].id && it.isLiked }
                newPlaylist[i] = newPlaylist[i].copy(isLiked = isLiked)
            }
            currentPlaylist = Playlist(newPlaylist)
            musicPlayer.play(track = track)
        }
    }

    override fun stop() {
        musicPlayer.stop()
    }


    override suspend fun playNext() {
        withContext(Dispatchers.IO) {
            if (currentPlaylist != null) {
                mutex.withLock {
                    if (currentPlaylist != null) {
                        var nextIndex: Int =
                            currentPlaylist!!.tracks.indexOf(currentTrack.value) + 1
                        if (_replayState.value == stepan.gorokhov.domain.models.ReplayState.ReplayPlaylist) {
                            nextIndex %= currentPlaylist!!.tracks.size
                            musicPlayer.play(currentPlaylist!!.tracks[nextIndex])
                        } else if (_replayState.value == stepan.gorokhov.domain.models.ReplayState.NoReplay) {
                            if (nextIndex < currentPlaylist!!.tracks.size) {
                                musicPlayer.play(currentPlaylist!!.tracks[nextIndex])
                            }
                        } else {
                            musicPlayer.play(currentPlaylist!!.tracks[nextIndex - 1])
                        }
                    }
                }
            }
        }
    }

    override suspend fun playPrevious() {
        withContext(Dispatchers.IO) {
            mutex.withLock {
                if (currentPlaylist != null) {
                    try {
                        val prevIndex: Int =
                            currentPlaylist!!.tracks.indexOf(currentTrack.value) - 1
                        musicPlayer.play(currentPlaylist!!.tracks[max(0, prevIndex)])
                    } catch (e: IndexOutOfBoundsException) {
                        Log.e("Rep impl", "previous error")
                    }
                }
            }
        }
    }

    override fun rewind(rewindValue: Float) {
        musicPlayer.changeRewind(rewindValue)
    }


}