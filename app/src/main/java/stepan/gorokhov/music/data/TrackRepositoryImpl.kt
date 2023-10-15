package stepan.gorokhov.music.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import stepan.gorokhov.music.data.datasources.TrackService
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.domain.repositories.TrackRepository
import stepan.gorokhov.music.utils.MusicPlayer
import javax.inject.Inject

class TrackRepositoryImpl @Inject constructor(
    private val trackService: TrackService,
    private val musicPlayer: MusicPlayer
) :
    TrackRepository {
    override val isPlaying: StateFlow<Boolean>
        get() = musicPlayer.isPlaying
    override val currentTrack: StateFlow<Track?> get() = musicPlayer.track
    override val trackProgress: StateFlow<Int> get() = musicPlayer.progress
    private var currentPlaylist:Playlist? = null
    private val repositoryScope = CoroutineScope(Dispatchers.Main)
    private val mutex = Mutex()
    init {
        musicPlayer.setOnCompletionListener {
            repositoryScope.launch {
                playNext()
            }
        }
    }
    override suspend fun getTracksByName(name: String): Result<Playlist> {
        return runCatching<Playlist> {
            val trackList = trackService.searchTrack(name)
            Playlist(tracks = trackList.trackList.map { Track(name = it.title, artists = listOf(Artist(it.artistName)),
                duration = it.duration, image = it.imageUrl, url = it.trackUrl, isLiked = false) })
        }
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
        withContext(Dispatchers.IO){
            if(currentPlaylist != null){
                mutex.withLock {
                    if (currentPlaylist != null){
                        var nextIndex:Int = currentPlaylist!!.tracks.indexOf(currentTrack.value)+1
                        nextIndex%=currentPlaylist!!.tracks.size
                        musicPlayer.play(currentPlaylist!!.tracks[nextIndex])
                    }
                }
            }
        }
    }


}