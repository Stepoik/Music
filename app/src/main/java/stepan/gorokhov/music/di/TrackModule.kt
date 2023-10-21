package stepan.gorokhov.music.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import stepan.gorokhov.music.data.TrackRepositoryImpl
import stepan.gorokhov.music.data.datasources.TrackService
import stepan.gorokhov.domain.repositories.TrackRepository


@Module
interface TrackModule {
    @AppScope
    @Binds
    fun provideTrackRepository(trackRepositoryImpl: TrackRepositoryImpl): TrackRepository
    companion object{
        @Provides
        fun provideGson():Gson{
            return GsonBuilder().create()
        }
        @Provides
        fun provideRetrofit(gson: Gson):Retrofit{
            return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(Constants.BASE_URL).build()
        }
        @Provides
        fun provideTrackService(retrofit: Retrofit):TrackService{
            return retrofit.create(TrackService::class.java)
        }
    }
}