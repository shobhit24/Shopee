package com.example.shopee.repository

import android.content.Context
import com.example.shopee.data.database.AppDatabase
import com.example.shopee.data.remote.APiInterface
import com.example.shopee.model.Data
import com.example.shopee.model.Product
import com.example.shopee.model.ResponseDTO
import com.example.shopee.util.Constants.API_KEY
import com.example.shopee.util.NetworkUtils
import javax.inject.Inject

class ProductRepository
@Inject constructor(
    private val aPiInterface: APiInterface,
    val appDatabase: AppDatabase,
    private val applicationContext: Context
) {
    suspend fun getProducts(): ResponseDTO {

        // Checking for Active Internet Connection
        if (NetworkUtils.isOnline(applicationContext)) {

            // If Active Internet -> Make an API call
            try {
                val result = aPiInterface.getProducts(API_KEY)

                if (result.isSuccessful && result.body() != null) {
                    // Save productList to local DB
                    appDatabase.productDao().insertAll(result.body()!!.data.items)

                    return ResponseDTO(data = result.body()!!.data, error = "", status = "")
                }
                return ResponseDTO(
                    data = Data(emptyList<Product>()),
                    error = result.errorBody().toString(),
                    status = ""
                )

            } catch (e: Exception) {
                return getProductListFromDB()
            }
        }
        // Else -> load form local DB
        return getProductListFromDB()
    }

    private suspend fun getProductListFromDB(): ResponseDTO {

        // Hit getAll query on DB
        val products = appDatabase.productDao().getAll()
        return if (products.isEmpty()) {
            ResponseDTO(
                Data(emptyList()), error = "Check you internet connection and refresh", status = ""
            )
        } else {
            ResponseDTO(Data(products), error = "", status = "")
        }
    }


    suspend fun searchProducts(searchText: String?): List<Product> {
        return appDatabase.productDao().searchProduct(searchText)
    }

}