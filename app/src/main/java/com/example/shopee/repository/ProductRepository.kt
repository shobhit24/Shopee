package com.example.shopee.repository

import android.content.Context
import com.example.shopee.data.database.AppDatabase
import com.example.shopee.data.remote.APiInterface
import com.example.shopee.model.Data
import com.example.shopee.model.Product
import com.example.shopee.model.ResponseDTO
import com.example.shopee.util.NetworkUtils
import javax.inject.Inject

class ProductRepository
@Inject constructor(
    private val aPiInterface: APiInterface,
    val appDatabase: AppDatabase,
    private val applicationContext: Context
) {
    suspend fun getProducts(): ResponseDTO? {

        // Checking for Active Internet Connection
        if (NetworkUtils.isOnline(applicationContext)) {

            // If Active Internet -> Make an API call
            return try {
                val result = aPiInterface.getProducts()

                // Save productList to local DB
                appDatabase.productDao().insertAll(result.body()!!.data.items)
                result.body() as ResponseDTO

            } catch (e: Exception) {
                getProductListFromDB()
            }
        }
        return getProductListFromDB()
    }

    private suspend fun getProductListFromDB(): ResponseDTO {

        // Hit getAll query on DB
        val products = appDatabase.productDao().getAll()
        return ResponseDTO(Data(products), "", "")
    }

    suspend fun searchProducts(searchText: String?): List<Product> {
        return appDatabase.productDao().searchProduct(searchText)
    }

}