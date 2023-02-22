package com.example.shopee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.model.Data
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

    // LiveData for swipeLeft and swipeRight state to hold & update the value in when swipe detected
    private val _swipeLeftObserver = MutableLiveData<Unit>()
    private val _swipeRightObserver = MutableLiveData<Unit>()

    val swipeLeftObserver = _swipeLeftObserver
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

    // updating the swipe direction LiveData with current fragment
    fun onSwipeLeft() {
        _swipeLeftObserver.postValue(Unit)
    }

    fun onSwipeRight() {
        _swipeRightObserver.postValue(Unit)
    }

    fun searchProducts(searchText: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            val searchResult =
                productRepository.appDatabase.productDao().searchProduct(searchText)
            productLiveData.postValue(
                ResponseDTO(
                    Data(items = searchResult),
                    error = "",
                    status = ""
                )
            )
        }
    }
}