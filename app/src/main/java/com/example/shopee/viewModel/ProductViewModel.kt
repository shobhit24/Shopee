package com.example.shopee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {
    init {
        callOnInit()
    }

    private var productLiveData = MutableLiveData<ResponseDTO?>()
    private val _swipeLeftObserver = MutableLiveData<String>()
    val swipeLeftObserver = _swipeLeftObserver
    private val _swipeRightObserver = MutableLiveData<String>()
    val swipeRightObserver = _swipeRightObserver

    val products: LiveData<ResponseDTO?>
        get() = productLiveData

    fun callOnInit() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = productRepository.getProducts()
            if (result != null) {
                productLiveData.postValue(result)
            }
        }
    }

    fun onSwipeLeft(currentView: String) {
        _swipeLeftObserver.postValue(currentView)
    }

    fun onSwipeRight(currentView: String) {
        _swipeRightObserver.postValue(currentView)
    }
}