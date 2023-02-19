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

    lateinit var appDatabase: AppDatabase
    lateinit var productDao: ProductDao

    @Before
    fun setUp() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        productDao = appDatabase.productDao()

    }

    @Test
    fun test_InsertProducts() = runBlocking {
        val products = listOf(
            Product(name = "productName", price = "100.0", id = 1, image = "", extra = ""),
        )
        productDao.insertAll(products)

        val result = productDao.getAll()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("productName", result[0].name)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }
}