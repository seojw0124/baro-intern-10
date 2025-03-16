package com.jeongu.barointernapp.presentation.model

import android.os.Parcelable
import com.jeongu.barointernapp.domain.model.Seller
import kotlinx.parcelize.Parcelize

@Parcelize
data class SellerModel(
    val id: Int,
    val name: String,
    val profileImageUrl: String,
    val mannerTemperature: Double
) : Parcelable

fun Seller.toPresentation(): SellerModel = SellerModel(
    id = id,
    name = name,
    profileImageUrl = profileImageUrl,
    mannerTemperature = mannerTemperature
)