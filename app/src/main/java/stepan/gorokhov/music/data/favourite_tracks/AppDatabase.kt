package stepan.gorokhov.music.data.favourite_tracks

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteTrackEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favouriteDao():FavouriteDao
}