package stepan.gorokhov.domain.models

import android.graphics.Bitmap

data class Track(
    val id:String,
    val name:String,
    val artists:List<Artist>,
    val url:String,
    val duration:Int = 120,
    val isLiked:Boolean,
    val image:String
)
