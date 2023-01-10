package com.zhigaras.ricandmortycomposable.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.zhigaras.ricandmortycomposable.ErrorItem
import com.zhigaras.ricandmortycomposable.LoadingItem
import com.zhigaras.ricandmortycomposable.LoadingView
import com.zhigaras.ricandmortycomposable.model.Location
import kotlinx.coroutines.flow.Flow

@Composable
fun LocationListScreen(
    pagedLocations: Flow<PagingData<Location>>
) {
    val lazyLocations = pagedLocations.collectAsLazyPagingItems()
    
    LazyColumn(
        modifier = Modifier
            .padding(4.dp)
    ) {
        items(lazyLocations) { location ->
            location?.let { LocationCard(location = it) }
        }
        lazyLocations.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = lazyLocations.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = lazyLocations.loadState.append as LoadState.Error
                    item {
                        e.error.localizedMessage?.let {
                            ErrorItem(
                                message = it,
                                onClickRetry = { retry() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocationCard(location: Location) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = location.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row {
                Text(text = location.gotType ?: "")
                Spacer(modifier = Modifier.size(16.dp))
                Text(text = location.gotDimension ?: "")
            }
        }
    }
}