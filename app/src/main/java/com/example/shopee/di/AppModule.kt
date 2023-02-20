package com.example.shopee.di

import android.content.Context
import com.example.shopee.data.database.AppDatabase
import com.example.shopee.data.remote.APiInterface
import com.example.shopee.data.remote.ApiUtilities
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getRetrofitInstance(): APiInterface =
        ApiUtilities.getInstace().create(APiInterface::class.java)

    @Provides
    @Singleton
    fun getDBInstance(@ApplicationContext ctx: Context): AppDatabase = AppDatabase.getDatabase(ctx)

    @Provides
    @Singleton
    fun getApplicationCtx(@ApplicationContext ctx: Context): Context = ctx
}