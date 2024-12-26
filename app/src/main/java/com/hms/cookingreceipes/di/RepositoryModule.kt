package com.hms.cookingreceipes.di

import com.hms.cookingreceipes.data.networking.ReceipesService
import com.hms.cookingreceipes.repository.ReceipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideReceipesRepository(receipesService: ReceipesService): ReceipesRepository =
        ReceipesRepository(receipesService)
}