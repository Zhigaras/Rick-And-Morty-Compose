package com.zhigaras.ricandmortycomposable.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.ricandmortycomposable.data.LocationsPagingSource
import com.zhigaras.ricandmortycomposable.data.PersonagesPagingSource
import com.zhigaras.ricandmortycomposable.data.Repository
import com.zhigaras.ricandmortycomposable.di.IoDispatcher
import com.zhigaras.ricandmortycomposable.model.Location
import com.zhigaras.ricandmortycomposable.model.Personage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonageViewModel @Inject constructor(
    private val repository: Repository,
    private val locationsPagingSource: LocationsPagingSource,
    private val personagesPagingSource: PersonagesPagingSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    
    val pagedPersonages: Flow<PagingData<Personage>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { personagesPagingSource }
    ).flow.cachedIn(viewModelScope)
    
    val pagedLocations: Flow<PagingData<Location>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { locationsPagingSource }
    ).flow.cachedIn(viewModelScope)
    
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _errorFlow.value = throwable.localizedMessage
    }
    
    private val _personageDetailsChannel = MutableStateFlow<Personage?>(null)
    val personageDetailsChannel = _personageDetailsChannel.asStateFlow()
    
    private val _errorFlow = MutableStateFlow<String?>(null)
    val errorFlow = _errorFlow.asStateFlow()
    
    fun getPersonageDetails(id: Int) {
        viewModelScope.launch(ioDispatcher + coroutineExceptionHandler) {
            val result = repository.getPersonageDetails(id)
            if (result.isSuccessful) {
                result.body()?.let { _personageDetailsChannel.value = it }
                _errorFlow.value = null
            }
        }
    }
}