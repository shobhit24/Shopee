package com.example.shopee.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shopee.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM item_table")
    fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<Product>)

}