package com.example.shopee.viewModel

import androidx.lifecycle.*
import com.example.shopee.model.Data
import com.example.shopee.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import com.example.shopee.util.ProductListState
import com.example.shopee.util.enums.ErrorType
import com.example.shopee.util.enums.SwipeDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {
    init {
        getScreenData()
    }

    private var productLiveData = MutableLiveData<ProductListState>()

    // LiveData for swipeLeft and swipeRight state to hold & update the value in when swipe detected
    private val _swipeObserver = MutableLiveData<SwipeDirection>()
    private var refreshing: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val isRefreshing = refreshing
    val swipeObserver = _swipeObserver

    val products: LiveData<ProductListState>
        get() = productLiveData


    private fun getScreenData() {
        viewModelScope.launch(Dispatchers.IO) {
            productLiveData.postValue(ProductListState.Loading)
            val result = productRepository.getProducts()
            productLiveData.postValue(handleProductsResponse(result))
        }
    }

    fun getSearchResults(searchText: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            productLiveData.postValue(ProductListState.Loading)
            val searchResult = productRepository.searchProducts(searchText)
            val productResponse = ResponseDTO(
                data = Data(searchResult), error = "", status = ""
            )
            productLiveData.postValue(handleProductsResponse(productResponse))
        }
    }

    private fun handleProductsResponse(productResponse: ResponseDTO): ProductListState {
        return if (productResponse.error.toString().isNotEmpty()) {
            ProductListState.Error(
                error = ErrorType.NETWORK_ERROR
            )
        } else if (productResponse.data.items.isEmpty()) {
            ProductListState.Error(
                error = ErrorType.SEARCH_NOT_FOUND
            )
        } else {
            ProductListState.Success(responseDTO = productResponse)
        }
    }

    // updating the swipe direction LiveData
    fun onSwipeLeft() {
        _swipeObserver.postValue(SwipeDirection.LEFT)
    }

    fun onSwipeRight() {
        _swipeObserver.postValue(SwipeDirection.RIGHT)
    }

    fun refreshProductList() {
        refreshing.postValue(true)
        viewModelScope.launch() {
            val result = productRepository.getProducts()
            productLiveData.postValue(handleProductsResponse(result))
            refreshing.postValue(false)
        }
    }


}