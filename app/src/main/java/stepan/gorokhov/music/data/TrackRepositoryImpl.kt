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
import stepan.gorokhov.music.data.datasources.TrackService
import stepan.gorokhov.utils.MusicPlayer
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackService: TrackService,
    private val musicPlayer: MusicPlayer
) :
    stepan.gorokhov.domain.repositories.TrackRepository {
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

    override fun likeCurrent() {
        if (currentPlaylist != null){
            currentPlaylist = currentPlaylist!!.copy(tracks = currentPlaylist!!.tracks.map {
                if (it == currentTrack.value){
                    it.copy(isLiked = !it.isLiked)
                }
                else{
                    it
                }
            })
            musicPlayer.likeTrack()
        }
    }

    override suspend fun getTracksByName(name: String): Result<Playlist> {
        return runCatching<Playlist> {
            val trackList = trackService.searchTrack(name)
            Playlist(tracks = trackList.trackList.map {
                Track(
                    name = it.title,
                    artists = listOf(stepan.gorokhov.domain.models.Artist(it.artistName)),
                    duration = it.duration,
                    image = it.imageUrl,
                    url = it.trackUrl,
                    isLiked = false
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
                        println(currentTrack.value)
                        println(nextIndex)
                        if (_replayState.value == stepan.gorokhov.domain.models.ReplayState.ReplayPlaylist){
                            nextIndex %= currentPlaylist!!.tracks.size
                            musicPlayer.play(currentPlaylist!!.tracks[nextIndex])
                        }
                        else if(_replayState.value == stepan.gorokhov.domain.models.ReplayState.NoReplay){
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

    override fun rewind(rewindValue: Float) {
        musicPlayer.changeRewind(rewindValue)
    }


}