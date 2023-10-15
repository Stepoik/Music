package stepan.gorokhov.music.data.datasources

import retrofit2.http.GET
import retrofit2.http.Query
import stepan.gorokhov.music.data.models.TrackListDto

interface TrackService {
    @GET("/music/search")
    suspend fun searchTrack(@Query("q") search:String):TrackListDto
}