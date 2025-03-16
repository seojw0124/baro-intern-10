package com.jeongu.barointernapp.di

import android.content.Context
import androidx.room.Room
import com.jeongu.barointernapp.data.datasource.local.AppDatabase
import com.jeongu.barointernapp.data.datasource.local.ProductDao
import com.jeongu.barointernapp.data.datasource.local.RoomConstant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LocalRoomModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            RoomConstant.ROOM_DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(appDatabase: AppDatabase): ProductDao = appDatabase.productDao()
}