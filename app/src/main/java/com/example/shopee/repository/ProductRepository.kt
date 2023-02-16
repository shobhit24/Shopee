package com.example.shopee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopee.database.AppDatabase
import com.example.shopee.remote.api.APiInterface
import com.example.shopee.remote.model.Data
import com.example.shopee.remote.model.ResponseDTO
import com.example.shopee.view.util.NetworkUtils

class ProductRepository(
    private val aPiInterface: APiInterface,
    private val appDatabase: AppDatabase,
    private val applicationContext: Context
) {
    private val itemLiveData = MutableLiveData<ResponseDTO>()

    val items: LiveData<ResponseDTO>
        get() = itemLiveData

    suspend fun getProducts() {

        // Checking for Active Internet Connection
        if (NetworkUtils.isOnline(applicationContext)) {

            // If Active Internet -> Make an API call
            val result = aPiInterface.getProducts()

            if (result.body() != null) {
                appDatabase.productDao().insertAll(result.body()!!.data.items)
                itemLiveData.postValue(result.body())
            }
        } else {

            // Else -> Fetch from DB
            val products = appDatabase.productDao().getAll()

            val productList = ResponseDTO(Data(products), "", "")
            itemLiveData.postValue(productList)
        }


    }


}