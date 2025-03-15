package com.jeongu.barointernapp.di

import com.jeongu.barointernapp.data.repository.ProductRepositoryImpl
import com.jeongu.barointernapp.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(source: ProductRepositoryImpl): ProductRepository
}