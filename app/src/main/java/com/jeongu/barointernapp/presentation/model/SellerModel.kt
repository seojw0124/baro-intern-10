package com.jeongu.barointernapp.presentation.model

import com.jeongu.barointernapp.domain.model.Seller

data class SellerModel(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val mannerTemperature: Double
)

fun Seller.toPresentation(): SellerModel = SellerModel(
    id = id,
    name = name,
    profileImageUrl = profileImageUrl,
    mannerTemperature = mannerTemperature
)