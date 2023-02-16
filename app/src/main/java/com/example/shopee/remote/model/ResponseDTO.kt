package com.example.shopee.remote.model

data class ResponseDTO(
    val `data`: Data,
    val error: Any,
    val status: String,
)