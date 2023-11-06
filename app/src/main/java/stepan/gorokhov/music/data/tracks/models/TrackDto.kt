package stepan.gorokhov.music.data.tracks.models

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("title")
    val title:String,
    @SerializedName("artist")
    val artistName:String,
    @SerializedName("duration")
    val duration:Int,
    @SerializedName("url")
    val trackUrl:String,
    @SerializedName("image_url")
    val imageUrl:String
)
