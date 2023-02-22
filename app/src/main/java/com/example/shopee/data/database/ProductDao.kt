package com.example.shopee.data.database

import androidx.room.*
import com.example.shopee.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM item_table")
    fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<Product>)

    @Query("SELECT DISTINCT * FROM item_table WHERE name LIKE  '%' || :searchText || '%' OR price LIKE '%' || :searchText || '%'")
    fun searchProduct(searchText: String?): List<Product>

}