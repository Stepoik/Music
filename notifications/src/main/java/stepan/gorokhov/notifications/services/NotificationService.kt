package stepan.gorokhov.notifications.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import stepan.gorokhov.notifications.DepsInjection

class NotificationService:Service() {
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.extras?.getInt("action")
        DepsInjection.notificationComponent?.apply {
            when(action){
                0->{
                    scope.launch {
                        trackRepository().playPrevious()
                    }
                }
                1->{
                    if (trackRepository().isPlaying.value){
                        trackRepository().stop()
                    }
                    else{
                        scope.launch {
                            trackRepository().play()
                        }
                    }
                }
                2-> {
                    scope.launch {
                        trackRepository().playNext()
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}