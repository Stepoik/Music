package stepan.gorokhov.music.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface RetrofitModule {
    companion object{
        @Provides
        fun provideGson(): Gson {
            return GsonBuilder().create()
        }
        @Provides
        fun provideRetrofit(gson: Gson): Retrofit {
            return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(Constants.BASE_URL).build()
        }
    }
}