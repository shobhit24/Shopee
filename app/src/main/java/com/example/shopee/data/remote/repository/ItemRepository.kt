package com.example.shopee.data.remote.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopee.api.APiInterface
import com.example.shopee.data.remote.model.ProductData

class ItemRepository (val aPiInterface: APiInterface){
    private val itemLiveData = MutableLiveData<ProductData>()

    val items: LiveData<ProductData>
    get() = itemLiveData

    suspend fun getProducts() {
        val result = aPiInterface.getProducts()
        if(result != null) {
            itemLiveData.postValue(result.body())
        }
    }


}