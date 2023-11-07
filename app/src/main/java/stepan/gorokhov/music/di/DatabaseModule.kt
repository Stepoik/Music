package stepan.gorokhov.music.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import stepan.gorokhov.music.data.favourite_tracks.AppDatabase
import stepan.gorokhov.music.data.favourite_tracks.FavouriteDao
import stepan.gorokhov.scopes.AppContext

@Module
interface DatabaseModule {
    companion object{
        @AppScope
        @Provides
        fun provideAppDatabase(context: Context):AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, "db").build()
        }
    }
}