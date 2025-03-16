package com.jeongu.barointernapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeongu.barointernapp.data.model.ProductLocal

@Database(entities = [ProductLocal::class], version = RoomConstant.ROOM_VERSION)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

}