package com.zhigaras.ricandmortycomposable.di

import com.zhigaras.ricandmortycomposable.data.PersonagesSearchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    
    @Provides
    @Singleton
    fun providesRetrofit(): PersonagesSearchApi {
        return Retrofit.Builder()
            .baseUrl(PersonagesSearchApi.BASE_URL)
            .client(
                OkHttpClient.Builder().build()
            )
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PersonagesSearchApi::class.java)
    }
}