package com.example.shopee.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopee.model.Data
import com.example.shopee.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import com.example.shopee.util.ProductListState
import com.example.shopee.util.enums.ErrorType
import com.example.shopee.util.enums.SwipeDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ProductViewModel is the single source of contact for the UI related data i.e. [Product List's State] [ProductListState]
 *
 * It has following member functions to be interacted with based upon the UI Events
 * - [getScreenData] - gets the data from the repository
 * - [getSearchResults] - gets the searched results from local database
 */
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private var productLiveData: MutableLiveData<ProductListState> =
        MutableLiveData<ProductListState>()

    init {
        getScreenData()
    }

    // LiveData for swipeLeft and swipeRight state to hold & update the value in when swipe detected
    private val _swipeObserver = MutableLiveData<SwipeDirection>()
    private var refreshing: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val isRefreshing = refreshing
    val swipeObserver = _swipeObserver

    val products: LiveData<ProductListState>
        get() = productLiveData


    /**
     * This method is responsible for fetching the product list from the repository
     */
    fun getScreenData() {
        productLiveData.value = ProductListState.Loading
        viewModelScope.launch {
            val result = productRepository.getProducts()
            productLiveData.value = handleProductsResponse(result)
        }
    }

    /**
     * This method is  responsible for searching the product based on [searchText].
     *
     * @param searchText is the textInput in the searchBar
     */
    fun getSearchResults(searchText: String?) {
        viewModelScope.launch {
            productLiveData.value = ProductListState.Loading
            val searchResult = productRepository.searchProducts(searchText)
            val productResponse = ResponseDTO(
                data = Data(searchResult), error = "", status = ""
            )
            productLiveData.value = handleProductsResponse(productResponse)
        }
    }

    /**
     * Handles the response from the repository and returns [ProductListState]
     */
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

    /**
     * Updates the LiveData value for swipeLeft gesture i.e from Right to Left
     */
    fun onSwipeLeft() {
        _swipeObserver.postValue(SwipeDirection.LEFT)
    }

    /**
     * Updates the LiveData value for swipeRight gesture i.e from Left to Right
     */
    fun onSwipeRight() {
        _swipeObserver.postValue(SwipeDirection.RIGHT)
    }

    /**
     * Refreshes the product list on SwipeToRefresh gesture and updates the value for [productLiveData]
     */
    fun refreshProductList() {
        refreshing.postValue(true)
        viewModelScope.launch {
            val result = productRepository.getProducts()
            productLiveData.value = handleProductsResponse(result)
            refreshing.postValue(false)
        }
    }


}