package com.devanasmohammed.search.data.remote.api

import com.devanasmohammed.search.data.model.ProductsResponse
import com.devanasmohammed.search.util.Constants.Companion.API_CALL_LIMIT
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductsApi {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int = API_CALL_LIMIT,
    ): Response<ProductsResponse>
}