package com.example.shopee

import com.example.shopee.data.remote.APiInterface
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiTest {

    private var mockWebServer: MockWebServer = MockWebServer()
    private lateinit var productAPiInterface: APiInterface

    @Before
    fun setUp() {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        productAPiInterface = Retrofit.Builder()
            .baseUrl(mockWebServer.url(path = "/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(APiInterface::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetProducts() = runTest {
        val mockResponse = MockResponse()
//        mockResponse.setBody("[]")
        mockWebServer.enqueue(mockResponse)
        val response = productAPiInterface.getProducts()
        mockWebServer.takeRequest()

        Assert.assertEquals(true, response.body()!!.data.items.isEmpty())
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}