package com.example.shopee.data.remote

import com.example.shopee.model.ResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("{api_key}")
    suspend fun getProducts(@Path("api_key") api_key: String): Response<ResponseDTO>
}