package stepan.gorokhov.music.data.models

import com.google.gson.annotations.SerializedName

data class TrackListDto(
    @SerializedName("tracks")
    val trackList:List<TrackDto>
)
