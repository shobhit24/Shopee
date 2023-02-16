package com.example.shopee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.remote.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository): ViewModel() {
    init {
        viewModelScope.launch (Dispatchers.IO){
            productRepository.getProducts()
        }
    }

    val items: LiveData<ResponseDTO>
    get() = productRepository.items
}