package stepan.gorokhov.music

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import stepan.gorokhov.notifications.Constants
import stepan.gorokhov.notifications.R

class MusicService: Service() {
    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(applicationContext, Constants.CHANNEL_ID)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.youtube)
            .setOngoing(true)
            .setContentTitle("PLS PLAY")
        startForeground(Constants.MUSIC_NOTIFICATION_ID,notification.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}