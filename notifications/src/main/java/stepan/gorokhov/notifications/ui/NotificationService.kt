package stepan.gorokhov.notifications.ui

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationService:Service() {
    private val scope = CoroutineScope(Dispatchers.Main)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val action = intent.extras?.getInt("action")
        println(action)
        DepsInjection.notificationComponent?.apply {
            if (action== 1) {
                if (trackRepository().isPlaying.value){
                    trackRepository().stop()
                }
                else{
                    scope.launch {
                        trackRepository().play()
                    }
                }
            }
            else if (action == 2){
                scope.launch {
                    trackRepository().playNext()
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}