package com.jeongu.barointernapp.di

import com.jeongu.barointernapp.data.datasource.remote.ProductRemoteDataSourceImpl
import com.jeongu.barointernapp.domain.datasource.ProductRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteDataSourceImpl {

    @Binds
    @Singleton
    abstract fun bindProductRemoteDataSource(source: ProductRemoteDataSourceImpl): ProductRemoteDataSource
}