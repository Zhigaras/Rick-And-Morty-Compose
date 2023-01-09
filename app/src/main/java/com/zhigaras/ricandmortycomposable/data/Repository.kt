package com.zhigaras.ricandmortycomposable.data

import com.zhigaras.ricandmortycomposable.model.ApiReply
import kotlinx.coroutines.delay
import retrofit2.Response

class Repository {
    suspend fun getPersonages(page: Int): Response<ApiReply> {
        delay(2000)
        return PersonagesApi.personagesSearchApi.findPersonages(page)
    }
}