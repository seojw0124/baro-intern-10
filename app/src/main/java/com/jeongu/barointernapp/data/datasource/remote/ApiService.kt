package com.jeongu.barointernapp.data.datasource.remote

import com.jeongu.barointernapp.data.model.ProductWrapperResponse
import retrofit2.http.GET

interface ApiService {

    suspend fun getProducts(): ProductWrapperResponse
}