package stepan.gorokhov.music.data.tracks.models

import com.google.gson.annotations.SerializedName
import stepan.gorokhov.domain.models.Artist
import stepan.gorokhov.domain.models.Track

data class TrackDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist")
    val artistName: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("url")
    val trackUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("is_liked")
    val isLiked: Boolean
) {
    fun toDomain(): Track {
        return Track(
            id = this.id,
            name = title,
            artists = listOf(Artist(artistName)),
            url = trackUrl,
            image = imageUrl,
            duration = duration,
            isLiked = isLiked
        )
    }
}
