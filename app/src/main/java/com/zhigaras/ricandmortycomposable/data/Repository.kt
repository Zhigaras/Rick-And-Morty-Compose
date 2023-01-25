package com.zhigaras.ricandmortycomposable.data

import com.zhigaras.ricandmortycomposable.model.LocationsApiReply
import com.zhigaras.ricandmortycomposable.model.Personage
import com.zhigaras.ricandmortycomposable.model.PersonagesApiReply
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val personagesSearchApi: PersonagesSearchApi) {
    suspend fun getPersonages(page: Int): Response<PersonagesApiReply> {
        return personagesSearchApi.getPersonages(page)
    }
    
    suspend fun getPersonageDetails(id: Int): Response<Personage> {
        return personagesSearchApi.getPersonageDetails(id)
    }
    
    suspend fun getLocations(page: Int): Response<LocationsApiReply> {
        return personagesSearchApi.getLocations(page)
    }
}