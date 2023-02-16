package com.example.shopee.api

import com.example.shopee.data.remote.model.ProductData
import retrofit2.Response
import retrofit2.http.GET

interface APiInterface {
    @GET("995ce2a0-1daf-4993-915f-8c198f3f752c")
    suspend fun getProducts() : Response<ProductData>
}