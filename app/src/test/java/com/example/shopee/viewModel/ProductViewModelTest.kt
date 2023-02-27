package com.example.shopee.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.shopee.getOrAwaitValue
import com.example.shopee.model.Data
import com.example.shopee.model.Product
import com.example.shopee.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import com.example.shopee.util.ProductListState
import com.example.shopee.util.enums.ErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ProductViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var productRepository: ProductRepository

    @Mock
    lateinit var product: Product

    @Mock
    private lateinit var liveDataObserverProductListState: Observer<ProductListState>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_GetProducts_NetworkError() = runTest {
        Mockito.`when`(productRepository.getProducts()).thenReturn(
            ResponseDTO(
                data = Data(items = emptyList()), error = "Network Error", status = ""
            )
        )
        val productViewModel = ProductViewModel(productRepository)

        productViewModel.getScreenData()
        productViewModel.products.observeForever(liveDataObserverProductListState)
        testDispatcher.scheduler.advanceUntilIdle()
        val response = productViewModel.products.getOrAwaitValue()
        assertNotNull(response)
        assertEquals(response, ProductListState.Error(error = ErrorType.NETWORK_ERROR))

    }

    @Test
    fun test_GetProducts() = runTest {
        Mockito.`when`(productRepository.getProducts()).thenReturn(
            ResponseDTO(
                data = Data(
                    items = listOf(
                        product
                    )
                ), error = "", status = ""
            )
        )
        val productViewModel = ProductViewModel(productRepository)
        productViewModel.getScreenData()
        productViewModel.products.observeForever(liveDataObserverProductListState)
        testDispatcher.scheduler.advanceUntilIdle()
        val response = productViewModel.products.getOrAwaitValue()

        assertEquals(true, response is ProductListState.Success)
    }

    @Test
    fun test_SearchProducts() = runTest {
        Mockito.`when`(productRepository.searchProducts("Item 1")).thenReturn(
            listOf(
                product,
            )
        )
        val productViewModel = ProductViewModel(productRepository)
        productViewModel.getSearchResults("Item 1")
        productViewModel.products.observeForever(liveDataObserverProductListState)
        testDispatcher.scheduler.advanceUntilIdle()
        val response = productViewModel.products.getOrAwaitValue()

        assertNotNull(response)
        assertEquals(true, response is ProductListState.Success)
    }

    @Test
    fun test_SearchProducts_SearchError() = runTest {
        Mockito.`when`(productRepository.searchProducts("Item 1")).thenReturn(
            emptyList()
        )
        val productViewModel = ProductViewModel(productRepository)
        productViewModel.getSearchResults("Item 1")
        productViewModel.products.observeForever(liveDataObserverProductListState)
        testDispatcher.scheduler.advanceUntilIdle()
        val response = productViewModel.products.getOrAwaitValue()

        assertEquals(response, ProductListState.Error(ErrorType.SEARCH_NOT_FOUND))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}