package com.jeongu.barointernapp.data.datasource.remote

import com.jeongu.barointernapp.data.model.ProductWrapperResponse

interface ApiService {

    suspend fun getProducts(): ProductWrapperResponse
}