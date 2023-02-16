package com.example.shopee.data.remote.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopee.data.remote.repository.ItemRepository

class ItemViewModelFactory(private val itemRepository: ItemRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ItemViewModel(itemRepository) as T
    }
}