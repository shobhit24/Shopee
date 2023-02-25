package com.example.shopee.util.enums

enum class ErrorType(val errorMessage: String) {
    NETWORK_ERROR("You are offline. Please connect to internet and try again"),
    SEARCH_NOT_FOUND("No items found for your search"),
}