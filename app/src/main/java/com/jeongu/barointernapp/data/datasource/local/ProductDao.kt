package com.jeongu.barointernapp.data.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeongu.barointernapp.data.model.ProductLocal

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(products: List<ProductLocal>)

    @Query("SELECT * FROM ${RoomConstant.Table.PRODUCTS} WHERE id = :productId")
    suspend fun getProduct(productId: Int): ProductLocal?

    @Query("SELECT * FROM ${RoomConstant.Table.PRODUCTS}")
    suspend fun getProducts(): List<ProductLocal>

    @Query("DELETE FROM ${RoomConstant.Table.PRODUCTS} WHERE id = :productId")
    suspend fun delete(productId: Int)

    @Query("UPDATE ${RoomConstant.Table.PRODUCTS} SET isLiked = :isLiked WHERE id = :productId")
    suspend fun updateLike(productId: Int, isLiked: Boolean)
}