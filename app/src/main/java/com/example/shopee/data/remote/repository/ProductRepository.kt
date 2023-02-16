package com.example.shopee.data.remote.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopee.data.local.model.AppDatabase
import com.example.shopee.data.remote.api.APiInterface
import com.example.shopee.data.remote.model.Data
import com.example.shopee.data.remote.model.ResponseDTO
import com.example.shopee.util.NetworkUtils

class ProductRepository(
    private val aPiInterface: APiInterface,
    private val appDatabase: AppDatabase,
    private val applicationContext: Context
) {
    private val itemLiveData = MutableLiveData<ResponseDTO>()

    val items: LiveData<ResponseDTO>
        get() = itemLiveData

    suspend fun getProducts() {
        if (NetworkUtils.isOnline(applicationContext)) {
            val result = aPiInterface.getProducts()
            if (result.body() != null) {
                appDatabase.productDao().insertAll(result.body()!!.data.items)
                itemLiveData.postValue(result.body())
            }
        } else {
            val products = appDatabase.productDao().getAll()
            val productList = ResponseDTO(Data(products), "", "")
            itemLiveData.postValue(productList)
        }


    }


}