package com.jeongu.barointernapp.di

import android.content.Context
import com.jeongu.barointernapp.data.datasource.remote.ApiService
import com.jeongu.barointernapp.data.datasource.remote.FakeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideFakeApiService(context: Context): FakeApiService {
        return FakeApiService(context)
    }

    @Provides
    @Singleton
    fun provideApiService(fakeApiService: FakeApiService): ApiService {
        return fakeApiService
    }
}