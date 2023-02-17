package com.example.shopee.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val price: String,
    @ColumnInfo val image: String,
    @ColumnInfo val extra: String?,
)