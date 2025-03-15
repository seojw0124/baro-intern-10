package com.jeongu.barointernapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jeongu.barointernapp.data.model.ProductLocal

@Dao
interface ProductDao {

    @Insert
    suspend fun insert(product: ProductLocal)

    @Query("SELECT * FROM ${RoomConstant.Table.PRODUCTS} WHERE id = :productId")
    suspend fun getProduct(productId: Long): ProductLocal?
}