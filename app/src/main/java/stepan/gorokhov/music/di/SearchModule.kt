package stepan.gorokhov.music.di

import dagger.Binds
import dagger.Module
import stepan.gorokhov.domain.repositories.SearchRepository
import stepan.gorokhov.music.data.SearchRepositoryImpl

@Module
interface SearchModule {

    @AppScope
    @Binds
    fun provideSearchRepository(searchRepositoryImpl: SearchRepositoryImpl):SearchRepository
}