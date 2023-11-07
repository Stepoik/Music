package stepan.gorokhov.music.data.favourite_tracks

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface FavouriteDao {
    @Query("select * from favourite_tracks where is_liked = 1")
    fun getFavouriteTracks():Flow<List<FavouriteTrackEntity>>

    @Query("select * from favourite_tracks")
    suspend fun getAllTracks():List<FavouriteTrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun likeTrack(trackEntity: FavouriteTrackEntity)
}