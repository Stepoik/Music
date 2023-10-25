package stepan.gorokhov.notifications.di

import android.content.Context
import dagger.Component
import stepan.gorokhov.domain.repositories.TrackRepository
import stepan.gorokhov.notifications.ui.Notifications
import javax.inject.Scope


interface NotificationDeps{
    val repository: TrackRepository
    val context:Context
}
@Scope
internal annotation class NotificationScope


@Component(dependencies = [NotificationDeps::class])
@NotificationScope
interface NotificationComponent {
    @Component.Factory
    interface Factory{
        fun create(notificationDeps: NotificationDeps): NotificationComponent
    }
    fun inject(notifications: Notifications)
    fun trackRepository():TrackRepository
}