package com.example.shopee.remote.api

import com.example.shopee.remote.model.ResponseDTO
import retrofit2.Response
import retrofit2.http.GET

interface APiInterface {
    @GET("995ce2a0-1daf-4993-915f-8c198f3f752c")
    suspend fun getProducts() : Response<ResponseDTO>
}