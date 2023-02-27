package com.example.shopee.data.database

import androidx.room.*
import com.example.shopee.model.Product

/**
 * This interface [ProductDao]  contains all the availed queries available to interact with the [Local Database][AppDatabase]
 * It has following suspend functions to interact
 * - [getAll] - returns the list of [products][Product]
 * - [insertAll] - inserts the [products][Product] list
 * - [searchProduct] - searches for the [product][Product] by any searchTerm
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM item_table")
    suspend fun getAll(): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT DISTINCT * FROM item_table WHERE name LIKE  '%' || :searchText || '%' OR price LIKE '%' || :searchText || '%'")
    suspend fun searchProduct(searchText: String?): List<Product>

}