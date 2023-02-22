package com.example.shopee.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopee.getOrAwaitValue
import com.example.shopee.model.Data
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
        sut.callOnInit()
        testDispatcher.scheduler.advanceUntilIdle()
        val result = sut.products.getOrAwaitValue()

        Assert.assertEquals(0, result?.data?.items?.size)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}