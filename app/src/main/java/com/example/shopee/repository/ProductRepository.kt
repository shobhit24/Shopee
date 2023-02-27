package com.example.shopee.repository

import android.content.Context
import com.example.shopee.data.database.AppDatabase
import com.example.shopee.data.remote.ApiInterface
import com.example.shopee.model.Data
import com.example.shopee.model.Product
import com.example.shopee.model.ResponseDTO
import com.example.shopee.util.Constants.API_KEY
import com.example.shopee.util.NetworkUtils
import javax.inject.Inject

/**
 * ProductRepository is the single source of contact for the data either from
 * - [Local Database] [AppDatabase] or
 * - [Remote][ApiInterface]
 *
 * Also it checks for the network connection on device
 *
 * - if online -> return the data from [Local Database] [AppDatabase]
 * - if offline -> return the data from [Remote][ApiInterface]
 */
class ProductRepository
@Inject constructor(
    private val apiInterface: ApiInterface,
    private val appDatabase: AppDatabase,
    private val applicationContext: Context
) {
    suspend fun getProducts(): ResponseDTO {

        // Checking for Active Internet Connection
        if (NetworkUtils.isOnline(applicationContext)) {

            // If Active Internet -> Make an API call
            try {
                val result = apiInterface.getProducts(API_KEY)

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