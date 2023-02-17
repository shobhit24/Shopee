package com.example.shopee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.getProducts()
        }
    }

    val products: LiveData<ResponseDTO>
        get() = productRepository.products
}