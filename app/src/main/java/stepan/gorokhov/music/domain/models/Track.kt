package stepan.gorokhov.music.domain.models

import android.graphics.Bitmap

data class Track(
    val name:String,
    val artists:List<Artist>,
    val url:String,
    val duration:Int = 120,
    val isLiked:Boolean,
    val image:String
)
