package stepan.gorokhov.music.data.favourite_tracks

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import stepan.gorokhov.domain.models.Artist
import stepan.gorokhov.domain.models.Track

@Entity(tableName = "favourite_tracks")
data class FavouriteTrackEntity(
    @PrimaryKey val id:String,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("artist")
    val artistName: String,
    @ColumnInfo("duration")
    val duration: Int,
    @ColumnInfo("url")
    val trackUrl: String,
    @ColumnInfo("image_url")
    val imageUrl: String,
    @ColumnInfo("is_liked")
    val isLiked: Boolean
){
    companion object{
        fun fromDomain(track:Track):FavouriteTrackEntity{
            return FavouriteTrackEntity(
                id = track.id,
                title = track.name,
                artistName = track.artists[0].name,
                duration = track.duration,
                trackUrl = track.url,
                imageUrl = track.image,
                isLiked = track.isLiked
            )
        }
    }
    fun toDomain():Track{
        return Track(
            id = this.id,
            name = this.title,
            artists = listOf(Artist( artistName)),
            duration = this.duration,
            url = this.trackUrl,
            image = this.imageUrl,
            isLiked = this.isLiked
        )
    }
}