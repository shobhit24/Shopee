package com.example.shopee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
    private val productLiveData = MutableLiveData<ResponseDTO>()

    val products: LiveData<ResponseDTO>
        get() = productLiveData

    suspend fun getProducts() {

        // Checking for Active Internet Connection
        if (NetworkUtils.isOnline(applicationContext)) {

            // If Active Internet -> Make an API call
            val result = aPiInterface.getProducts()

            if (result.body() != null) {
                appDatabase.productDao().insertAll(result.body()!!.data.items)
                productLiveData.postValue(result.body())
            }
        } else {

            // Else -> Fetch from DB
            val products = appDatabase.productDao().getAll()

            val productList = ResponseDTO(Data(products), "", "")
            productLiveData.postValue(productList)
        }

    }


}