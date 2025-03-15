package com.jeongu.barointernapp.data.model

//data class ProductResponse(
//    val id: Int,
//    val image: String,
//    val title: String,
//    val introduction: String,
//    val price: Int,
//    @SerializedName("trading_place") val tradingPlace: String,
//    @SerializedName("like_count") val likeCount: Int,
//    @SerializedName("comment_count") val commentCount: Int,
//    val isLiked: Boolean,
//    val seller: SellerResponse
//)
data class ProductEntity(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val introduction: String,
    val price: Int,
    val tradingPlace: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLiked: Boolean,
    val seller: SellerEntity
)

data class SellerEntity(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val mannerTemperature: Double
)
