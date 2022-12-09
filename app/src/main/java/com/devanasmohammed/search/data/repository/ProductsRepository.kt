package com.devanasmohammed.search.data.repository

import android.util.Log
import com.devanasmohammed.search.data.model.ProductsResponse
import com.devanasmohammed.search.data.remote.RetrofitInstance
import retrofit2.Response

class ProductsRepository() {

    private val tag = "ProductsRepository"

    suspend fun getRemoteProducts(): Response<ProductsResponse>? {
        try {
            return RetrofitInstance.api.getProducts()
        } catch (e: Exception) {
            Log.e(tag, "Catch error in getRemoteProducts ${e.message}")
        }
        return null
    }
}

