package com.jeongu.barointernapp.di

import com.jeongu.barointernapp.data.datasource.local.ProductLocalDataSourceImpl
import com.jeongu.barointernapp.domain.datasource.ProductLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindProductLocalDataSource(source: ProductLocalDataSourceImpl): ProductLocalDataSource
}