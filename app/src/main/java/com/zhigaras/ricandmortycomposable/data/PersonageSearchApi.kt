package com.zhigaras.ricandmortycomposable.data

import com.zhigaras.ricandmortycomposable.model.LocationsApiReply
import com.zhigaras.ricandmortycomposable.model.Personage
import com.zhigaras.ricandmortycomposable.model.PersonagesApiReply
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PersonagesSearchApi {
    
    companion object {
        internal const val BASE_URL = "https://rickandmortyapi.com/api/"
    }
    
    @GET("character")
    suspend fun getPersonages(@Query("page") page: Int): Response<PersonagesApiReply>
    
    @GET("character/{id}")
    suspend fun getPersonageDetails(@Path("id") id: Int): Response<Personage>
    
    @GET("location")
    suspend fun getLocations(@Query("page") page: Int): Response<LocationsApiReply>
}