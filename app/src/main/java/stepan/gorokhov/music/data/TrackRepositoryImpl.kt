package stepan.gorokhov.music.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import stepan.gorokhov.music.data.datasources.TrackService
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.ReplayState
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.domain.repositories.TrackRepository
import stepan.gorokhov.music.utils.MusicPlayer
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackService: TrackService,
    private val musicPlayer: MusicPlayer
) :
    TrackRepository {
    private val _replayState = MutableStateFlow(ReplayState.NoReplay)
    override val replayState: StateFlow<ReplayState>
        get() = _replayState

    override val isPlaying: StateFlow<Boolean>
        get() = musicPlayer.isPlaying
    override val currentTrack: StateFlow<Track?> get() = musicPlayer.track
    override val trackProgress: StateFlow<Int> get() = musicPlayer.progress
    private var currentPlaylist: Playlist? = null
    private val repositoryScope = CoroutineScope(Dispatchers.Main)
    private val mutex = Mutex()

    init {
        musicPlayer.setOnCompletionListener {
            repositoryScope.launch {
                playNext()
            }
        }
    }

    override fun nextReplayState() {
        _replayState.value = _replayState.value.next()
    }

    override suspend fun getTracksByName(name: String): Result<Playlist> {
        return runCatching<Playlist> {
            val trackList = trackService.searchTrack(name)
            Playlist(tracks = trackList.trackList.map {
                Track(
                    name = it.title, artists = listOf(Artist(it.artistName)),
                    duration = it.duration, image = it.imageUrl, url = it.trackUrl, isLiked = false
                )
            })
        }
    }

    override suspend fun getFavoritePlaylist(): Result<Playlist> {
        return getTracksByName("morgenshtern")
    }

    override suspend fun play() {
        musicPlayer.play()
    }

    override suspend fun play(track: Track, playlist: Playlist) {
        mutex.withLock {
            currentPlaylist = playlist
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
                        if (_replayState.value == ReplayState.ReplayPlaylist){
                            nextIndex %= currentPlaylist!!.tracks.size
                            musicPlayer.play(currentPlaylist!!.tracks[nextIndex])
                        }
                        else if(_replayState.value == ReplayState.NoReplay){
                            if (nextIndex < currentPlaylist!!.tracks.size){
                                musicPlayer.play(currentPlaylist!!.tracks[nextIndex])
                            }
                        }
                        else{
                            musicPlayer.play(currentPlaylist!!.tracks[nextIndex-1])
                        }
                    }
                }
            }
        }
    }


}