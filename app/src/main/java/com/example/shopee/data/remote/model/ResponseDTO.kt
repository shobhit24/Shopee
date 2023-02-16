package com.example.shopee.data.remote.model

data class ResponseDTO(
    val `data`: Data,
    val error: Any,
    val status: String,
)