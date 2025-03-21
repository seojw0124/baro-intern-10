package com.jeongu.barointernapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeongu.barointernapp.data.datasource.local.LocalMapper
import com.jeongu.barointernapp.data.datasource.local.RoomConstant

@Entity(tableName = RoomConstant.Table.PRODUCTS)
data class ProductLocal(
    @PrimaryKey val id: Int,
    val imageUrl: String,
    val title: String,
    val introduction: String,
    val price: Int,
    val tradingPlace: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val seller: SellerLocal
) : LocalMapper<ProductEntity> {
    override fun toData(): ProductEntity =
        ProductEntity(
            id,
            imageUrl,
            title,
            introduction,
            price,
            tradingPlace,
            likeCount,
            commentCount,
            isLiked,
            seller.toData()
        )
}

fun ProductEntity.toLocal() = ProductLocal(
    id = id,
    imageUrl = imageUrl,
    title = title,
    introduction = introduction,
    price = price,
    tradingPlace = tradingPlace,
    likeCount = likeCount,
    commentCount = commentCount,
    isLiked = isLiked,
    seller = seller.toLocal()
)
