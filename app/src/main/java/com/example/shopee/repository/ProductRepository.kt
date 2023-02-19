package com.example.shopee.repository

import android.content.Context
import com.example.shopee.data.database.AppDatabase
import com.example.shopee.data.remote.APiInterface
import com.example.shopee.model.Data
import com.example.shopee.model.ResponseDTO
import com.example.shopee.util.NetworkUtils
import javax.inject.Inject

class ProductRepository
@Inject constructor(
    private val aPiInterface: APiInterface,
    private val appDatabase: AppDatabase,
    private val applicationContext: Context
) {
    suspend fun getProducts(): ResponseDTO? {

        // Checking for Active Internet Connection
        if (NetworkUtils.isOnline(applicationContext)) {

            // If Active Internet -> Make an API call
            val result = aPiInterface.getProducts()

            if (result.body() != null) {
                // Save productList to local DB
                appDatabase.productDao().insertAll(result.body()!!.data.items)
                return result.body() as ResponseDTO
            }
            return null
        } else {

            // Else -> Fetch from DB
            val products = appDatabase.productDao().getAll()
            return ResponseDTO(Data(products), "", "")
        }

    }


}