package com.example.shopee.model

data class ResponseDTO(
    val `data`: Data,
    val error: Any,
    val status: String,
)

data class Data(
    val items: List<Product>
)