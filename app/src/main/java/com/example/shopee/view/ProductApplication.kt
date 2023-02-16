package com.example.shopee.view

import android.app.Application
import com.example.shopee.data.local.model.AppDatabase
import com.example.shopee.data.remote.api.APiInterface
import com.example.shopee.data.remote.api.ApiUtilities
import com.example.shopee.data.remote.repository.ProductRepository

class ProductApplication : Application() {

    lateinit var productRepository: ProductRepository

    override fun onCreate() {
        super.onCreate()
        initialise()
    }

    private fun initialise() {
        val apiInterface = ApiUtilities.getInstace().create(APiInterface::class.java)
        val database = AppDatabase.getDatabase(applicationContext)
        productRepository = ProductRepository(apiInterface, database, applicationContext)
    }
}