package stepan.gorokhov.notifications.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.notifications.Constants
import stepan.gorokhov.notifications.DepsInjection
import stepan.gorokhov.notifications.R
import stepan.gorokhov.notifications.di.NotificationComponent
import stepan.gorokhov.notifications.services.NotificationService
import javax.inject.Inject


class Notifications(private val notificationComponent: NotificationComponent) {
    @Inject
    lateinit var trackRepository: TrackRepository

    @Inject
    lateinit var context: Context
    private val notificationScope = CoroutineScope(Dispatchers.Main)
    private val notificationManager: NotificationManager

    init {
        DepsInjection.notificationComponent = notificationComponent
        notificationComponent.inject(this)

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationScope.launch {
            trackRepository.currentTrack.combine(trackRepository.isPlaying) { track, isPlaying ->
                Pair(track, isPlaying)
            }.collect {
                val track = it.first
                val isPlaying = it.second
                track?.apply {
                    sendNotification(track, isPlaying)
                }
            }
        }
        createNotificationChannel(notificationManager)

    }

    private suspend fun sendNotification(track: Track, isPlaying: Boolean) {
        val pauseIntent = Intent(context, NotificationService::class.java)
        pauseIntent.putExtra("action", 1)
        val nextIntent = Intent(context, NotificationService::class.java)
        nextIntent.putExtra("action", 2)
        val previousIntent = Intent(context, NotificationService::class.java)
        previousIntent.putExtra("action", 0)
        val previousPendingIntent =
            PendingIntent.getService(context, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val pausePendingIntent = PendingIntent.getService(
            context,
            1, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val nextPendingIntent = PendingIntent.getService(
            context,
            2, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val playStopIcon = if (isPlaying) R.drawable.stop_icon else R.drawable.play
        val backgroundImage: Bitmap
        withContext(Dispatchers.IO) {
            backgroundImage = Glide.with(context).asBitmap().load(track.image).submit().get()
        }
        val builder = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.youtube)
            .setContentTitle(track.name)
            .setContentText(track.artists[0].name)
            .addAction(R.drawable.previous, "Previous", previousPendingIntent)
            .addAction(playStopIcon, "Pause", pausePendingIntent)
            .addAction(R.drawable.next, "Next", nextPendingIntent)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setLargeIcon(backgroundImage)
        val notification = builder.build()
        notificationManager.notify(Constants.MUSIC_NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.CHANNEL_ID,
                "music",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}