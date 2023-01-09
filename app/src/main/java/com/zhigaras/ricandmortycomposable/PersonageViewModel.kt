package com.zhigaras.ricandmortycomposable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zhigaras.ricandmortycomposable.data.PersonagesPagingSource
import com.zhigaras.ricandmortycomposable.model.Personage
import kotlinx.coroutines.flow.Flow

class PersonageViewModel: ViewModel() {
    
    val pagedPersonages: Flow<PagingData<Personage>> = Pager(
        config = PagingConfig(20),
        pagingSourceFactory = { PersonagesPagingSource() }
    ).flow.cachedIn(viewModelScope)
}