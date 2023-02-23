package com.example.shopee.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopee.getOrAwaitValue
import com.example.shopee.model.Data
import com.example.shopee.model.Product
import com.example.shopee.model.ResponseDTO
import com.example.shopee.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
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

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun test_GetProducts() = runTest {
        Mockito.`when`(productRepository.getProducts())
            .thenReturn(ResponseDTO(data = Data(items = emptyList()), error = "", status = ""))

        val sut = ProductViewModel(productRepository)
        sut.onInit()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()

        Assert.assertEquals(0, result?.data?.items?.size)

    }

    @Test
    fun test_SearchProducts() = runTest {
        Mockito.`when`(productRepository.searchProducts("100"))
            .thenReturn(
                listOf(
                    Product(
                        id = 1,
                        name = "Item 1",
                        price = "100",
                        image = "",
                        extra = null
                    )
                )
            )

        val sut = ProductViewModel(productRepository)
        sut.searchProducts("100")
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()

        Assert.assertEquals(1, result?.data?.items?.size)
        Assert.assertEquals("100", result?.data?.items?.get(0)?.price ?: "")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}