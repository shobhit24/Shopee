package com.example.shopee.view.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopee.repository.ProductRepository
import com.example.shopee.viewModel.ProductViewModel

class ProductViewModelFactory(private val productRepository: ProductRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProductViewModel(productRepository) as T
    }
}