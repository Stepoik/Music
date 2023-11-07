package stepan.gorokhov.music.data.tracks.datasources

import retrofit2.http.GET
import retrofit2.http.Query
import stepan.gorokhov.music.data.tracks.models.TrackListDto

interface TrackService {
    @GET("/music/search")
    suspend fun searchTrack(@Query("q") search:String): TrackListDto
}