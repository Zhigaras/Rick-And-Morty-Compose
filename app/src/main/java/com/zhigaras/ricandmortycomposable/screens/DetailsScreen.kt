package com.zhigaras.ricandmortycomposable.screens

import android.icu.text.CaseMap.Title
import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.zhigaras.ricandmortycomposable.PersonageViewModel
import com.zhigaras.ricandmortycomposable.R
import com.zhigaras.ricandmortycomposable.Test
import com.zhigaras.ricandmortycomposable.ui.theme.RicAndMortyComposableTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.receiveAsFlow


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailsScreen(
    personageId: Int,
    personageViewModel: PersonageViewModel = viewModel()
) {
    personageViewModel.getPersonageDetails(personageId)
    
    val personage = personageViewModel.personageDetailsChannel.collectAsState().value
    
    Column() {
        personage?.let { it ->
            GlideImage(
                model = Uri.parse(it.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) { requestBuilder ->
                requestBuilder.placeholder(R.drawable.avatar_placeholder)
            }
            Column(Modifier.padding(start = 16.dp)) {
                Text(
                    text = it.name,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
    
                Text(text = stringResource(R.string.status), style = titleTextStyle)
    
                Row() {
                    Image(
                        painter = painterResource(id = it.statusMarker),
                        contentDescription = null,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = it.status,
                        modifier = Modifier.padding(start = 8.dp),
                        style = descTextStyle
                    )
                }
                DetailsBlock(title = R.string.species_gender, desc = "${it.species}(${it.gender})")
                DetailsBlock(title = R.string.origin, desc = it.origin.name)
                DetailsBlock(title = R.string.last_location, desc = it.location.name)
            }
        }
    }
}

@Composable
fun DetailsBlock(
    @StringRes title: Int,
    desc: String
) {
    Spacer(modifier = Modifier.size(16.dp))
    Text(
        text = stringResource(title),
        style = titleTextStyle
    )
    Text(text = desc, style = descTextStyle)
}

val titleTextStyle = TextStyle(
    color = Color.Gray,
    fontSize = 16.sp
)

val descTextStyle = TextStyle(
    fontSize = 20.sp
)
