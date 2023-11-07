package stepan.gorokhov.music.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import stepan.gorokhov.domain.repositories.FavouriteRepository
import stepan.gorokhov.music.data.FavouriteRepositoryImpl
import stepan.gorokhov.music.data.favourite_tracks.AppDatabase
import stepan.gorokhov.music.data.favourite_tracks.FavouriteDao

@Module
interface FavouriteModule {
    @AppScope
    @Binds
    fun provideFavouriteRepository(favouriteRepositoryImpl: FavouriteRepositoryImpl):FavouriteRepository
    companion object{
        @AppScope
        @Provides
        fun provideFavouriteDao(appDatabase: AppDatabase): FavouriteDao {
            return appDatabase.favouriteDao()
        }
    }
}