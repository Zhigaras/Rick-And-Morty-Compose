package com.zhigaras.ricandmortycomposable

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.ricandmortycomposable.data.PersonagesPagingSource
import com.zhigaras.ricandmortycomposable.data.Repository
import com.zhigaras.ricandmortycomposable.model.Personage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PersonageViewModel: ViewModel() {
    
    
    
    val pagedPersonages: Flow<PagingData<Personage>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { PersonagesPagingSource() }
    ).flow.cachedIn(viewModelScope)
    
    private val _personageDetailsChannel = MutableStateFlow<Personage?>(null)
    val personageDetailsChannel = _personageDetailsChannel.asStateFlow()
    
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()
    
    fun getPersonageDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val result = Repository.getPersonageDetails(id)
            if (result.isSuccessful) {
                result.body()?.let { _personageDetailsChannel.value = it }
            } else {
                _errorChannel.send(result.message())
            }
            _isLoading.value = false
        }
    }
}