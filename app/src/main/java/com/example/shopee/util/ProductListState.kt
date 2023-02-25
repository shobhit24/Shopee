package com.example.shopee.util

import com.example.shopee.model.ResponseDTO
import com.example.shopee.util.enums.ErrorType

sealed class ProductListState {
    data class Success(val responseDTO: ResponseDTO) : ProductListState()
    data class Error(val error: ErrorType) : ProductListState()
    object Loading : ProductListState()
}