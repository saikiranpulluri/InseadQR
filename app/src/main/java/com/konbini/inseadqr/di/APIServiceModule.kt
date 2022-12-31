package com.konbini.inseadqr.di

import com.konbini.inseadqr.data.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object APIServiceModule {
    @Provides
    fun provideCategoryService(retrofit: Retrofit): APIService =
        retrofit.create(APIService::class.java)
}