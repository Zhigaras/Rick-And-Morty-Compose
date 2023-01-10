package com.zhigaras.ricandmortycomposable.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.zhigaras.ricandmortycomposable.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.ricandmortycomposable.ErrorItem
import com.zhigaras.ricandmortycomposable.LoadingItem
import com.zhigaras.ricandmortycomposable.LoadingView
import com.zhigaras.ricandmortycomposable.model.Personage
import kotlinx.coroutines.flow.Flow

@Composable
fun PersonageListScreen(
    pagedPersonages: Flow<PagingData<Personage>>,
    onPersonageClick: (Int) -> Unit = {}
) {
    val lazyPersonages = pagedPersonages.collectAsLazyPagingItems()
    
    LazyColumn(
        modifier = Modifier
            .padding(4.dp)
    ) {
        items(lazyPersonages) { personage ->
            personage?.let {
                PersonageCard(
                    personage = it,
                    onCardClick = onPersonageClick
                )
            }
        }
        lazyPersonages.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    item { LoadingItem() }
                }
                loadState.refresh is LoadState.Error -> {
                    val e = lazyPersonages.loadState.refresh as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            modifier = Modifier.fillParentMaxSize(),
                            onClickRetry = { retry() }
                        )
                    }
                }
                loadState.append is LoadState.Error -> {
                    val e = lazyPersonages.loadState.append as LoadState.Error
                    item {
                        ErrorItem(
                            message = e.error.localizedMessage!!,
                            onClickRetry = { retry() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PersonageCard(
    personage: Personage,
    onCardClick: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(personage.id) }
            .padding(4.dp)
    ) {
        Row() {
            GlideImage(model = Uri.parse(personage.image), contentDescription = null) {
                it.placeholder(R.drawable.avatar_placeholder)
            }
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxHeight()
            ) {
                Text(text = personage.name, style = MaterialTheme.typography.titleMedium)
                Row() {
                    Image(
                        painter = painterResource(id = personage.statusMarker),
                        contentDescription = null,
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = personage.status + " | " + personage.species,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Text(
                    text = "Origin:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(text = personage.origin.name)
            }
        }
    }
}