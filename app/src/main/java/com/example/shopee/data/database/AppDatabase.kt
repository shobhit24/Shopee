package com.example.shopee.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shopee.model.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(ctx: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(ctx, AppDatabase::class.java, "appDB").build()
                }
            }
            return INSTANCE!!
        }
    }

}