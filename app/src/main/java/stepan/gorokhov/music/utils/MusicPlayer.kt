package stepan.gorokhov.music.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaPlayer.OnInfoListener
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import stepan.gorokhov.music.R
import stepan.gorokhov.music.di.AppContext
import stepan.gorokhov.music.di.AppScope
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Track
import java.lang.IllegalStateException
import javax.inject.Inject


@AppScope
class MusicPlayer @Inject constructor(@AppContext private val context: Context) {
    private var mediaPlayer = initMediaPlayer()
    private val scope = CoroutineScope(Dispatchers.Main)
    private val _track = MutableStateFlow<Track?>(null)
    val track: StateFlow<Track?> get() = _track
    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> get() = _progress
    private var onCompletion:()->Unit = {}
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying:StateFlow<Boolean> get() = _isPlaying
    fun setOnCompletionListener(onCompletion:()->Unit){
        this.onCompletion = onCompletion
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }
    private fun initMediaPlayer():MediaPlayer{
        return MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }
    suspend fun play(track: Track? =  null) {
        var isError = false
        withContext(Dispatchers.IO) {
            try {
                if (track != null) {
                    scope.coroutineContext.cancelChildren()
                    try {
                        mediaPlayer.release()
                    } catch (e: Exception) {
                        println(e)
                    }
                    mediaPlayer = initMediaPlayer()
                    mediaPlayer.setOnCompletionListener {
                        onCompletion()
                    }
                    mediaPlayer.setDataSource(track.url)
                    mediaPlayer.prepare()
                    _track.value = track
                }
            }
            catch (e: IllegalStateException){
                isError = true
            }
        }
        if (!isError) {
            _isPlaying.value = true
            mediaPlayer.start()
            progressUpdate()
        }
    }


    private fun progressUpdate() {
        scope.launch(Dispatchers.IO) {
            while (isActive && mediaPlayer.isPlaying) {
                try{
                    _progress.value = mediaPlayer.currentPosition / 1000
                }
                catch (e: IllegalStateException){
                    Log.e("MUSIC PLAYER", e.message.toString())
                    break
                }
                delay(100)
            }
        }
    }

    fun stop() {
        mediaPlayer.pause()
        scope.coroutineContext.cancelChildren()
        _isPlaying.value = false
    }
}