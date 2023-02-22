package com.example.shopee

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.shopee.data.database.AppDatabase
import com.example.shopee.data.database.ProductDao
import com.example.shopee.model.Product
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ProductDaoTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var productDao: ProductDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        productDao = appDatabase.productDao()

    }

    @Test
    fun test_GetAllProducts_EmptyResult() = runBlocking {
        val result = productDao.getAll()

        Assert.assertEquals(true, result.isEmpty())
    }

    @Test
    fun test_InsertProducts() = runBlocking {
        val products = listOf(
            Product(name = "Item 1", price = "100.0", id = 1, image = "", extra = ""),
        )
        productDao.insertAll(products)

        val result = productDao.getAll()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Item 1", result[0].name)
    }

    @Test
    fun test_GetAllProducts_EmptyProductList() = runBlocking {
        val result = productDao.getAll()

        Assert.assertEquals(0, result.size)
    }

    @Test
    fun test_SearchProductByNameOrPrice_EmptyResult() = runBlocking {
        val products = listOf(
            Product(name = "Item 1", price = "100.0", id = 1, image = "", extra = ""),
            Product(name = "Item 2", price = "200.0", id = 2, image = "", extra = ""),
        )

        productDao.insertAll(products)
        val searchByNameResults = productDao.searchProduct(searchText = "Item 10")
        val searchByPriceResult = productDao.searchProduct(searchText = "1000")

        Assert.assertEquals(0, searchByNameResults.size)
        Assert.assertEquals(0, searchByPriceResult.size)
    }

    @Test
    fun test_SearchProductByProductName() = runBlocking {
        val products = listOf(
            Product(name = "Item 1", price = "100.0", id = 1, image = "", extra = ""),
            Product(name = "Item 2", price = "200.0", id = 2, image = "", extra = ""),
            Product(name = "Item 3", price = "300.0", id = 3, image = "", extra = ""),
        )
        productDao.insertAll(products)
        val searchResults = productDao.searchProduct(searchText = "Item 1")

        Assert.assertEquals(1, searchResults.size)
        Assert.assertEquals("Item 1", searchResults[0].name)

    }

    @Test
    fun test_SearchProductByPrice() = runBlocking {
        val products = listOf(
            Product(name = "Item 1", price = "100.0", id = 1, image = "", extra = ""),
            Product(name = "Item 2", price = "200.0", id = 2, image = "", extra = ""),
            Product(name = "Item 3", price = "300.0", id = 3, image = "", extra = ""),
            Product(name = "Item 4", price = "100.0", id = 4, image = "", extra = ""),
        )
        productDao.insertAll(products)
        val searchResults = productDao.searchProduct(searchText = "100")

        Assert.assertEquals(2, searchResults.size)
        Assert.assertEquals("100.0", searchResults[0].price)
        Assert.assertEquals("100.0", searchResults[1].price)

    }

    @After
    fun tearDown() {
        appDatabase.close()
    }
}