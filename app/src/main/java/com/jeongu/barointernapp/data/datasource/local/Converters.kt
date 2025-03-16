package com.jeongu.barointernapp.data.datasource.local

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.jeongu.barointernapp.data.model.ProductLocal
import com.jeongu.barointernapp.data.model.SellerLocal

@TypeConverters
class Converters {
    @TypeConverter
    fun fromProduct(product: ProductLocal): String {
        return Gson().toJson(product)
    }

    @TypeConverter
    fun toProduct(productString: String): ProductLocal {
        return Gson().fromJson(productString, ProductLocal::class.java)
    }

    @TypeConverter
    fun fromSeller(seller: SellerLocal): String {
        return Gson().toJson(seller)
    }

    @TypeConverter
    fun toSeller(sellerString: String): SellerLocal {
        return Gson().fromJson(sellerString, SellerLocal::class.java)
    }
}