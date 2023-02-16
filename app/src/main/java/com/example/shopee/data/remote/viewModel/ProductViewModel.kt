package com.example.shopee.data.remote.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.data.remote.model.ResponseDTO
import com.example.shopee.data.remote.repository.ProductRepository
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