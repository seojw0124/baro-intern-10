package com.jeongu.barointernapp.data.datasource.remote

import android.content.Context
import com.google.gson.Gson
import com.jeongu.barointernapp.data.model.ProductWrapperResponse

class FakeApiService(private val context: Context) : ApiService {
    override suspend fun getProducts(): ProductWrapperResponse {
        return getResponse("home.json")
    }

    private inline fun <reified T> getResponse(fileName: String): T {
        context.assets.open(fileName).use { inputStream ->
            val size: Int = inputStream.available()
            val buffer = ByteArray(size).apply {
                inputStream.read(this)
            }
            return String(buffer).fromJson<T>()!!
        }
    }

    private inline fun <reified T> String.fromJson(): T? {
        return try {
            Gson().fromJson(this, T::class.java)
        } catch (e: Exception) {
            null
        }
    }
}