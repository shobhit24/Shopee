package com.example.shopee.data.remote.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.data.remote.model.ProductData
import com.example.shopee.data.remote.repository.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemViewModel(private val itemRepository: ItemRepository): ViewModel() {
    init {
        viewModelScope.launch (Dispatchers.IO){
            itemRepository.getProducts()
        }
    }

    val items: LiveData<ProductData>
    get() = itemRepository.items
}