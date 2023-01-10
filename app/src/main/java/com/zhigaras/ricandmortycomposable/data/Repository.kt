package com.zhigaras.ricandmortycomposable.data

import com.zhigaras.ricandmortycomposable.model.ApiReply
import com.zhigaras.ricandmortycomposable.model.Personage
import kotlinx.coroutines.delay
import retrofit2.Response

object Repository {
    suspend fun getPersonages(page: Int): Response<ApiReply> {
        delay(2000)
        return PersonagesApi.personagesSearchApi.findPersonages(page)
    }
    
    suspend fun getPersonageDetails(id: Int) : Response<Personage> {
        return PersonagesApi.personagesSearchApi.getPersonageDetails(id)
    }
}