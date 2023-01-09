package com.zhigaras.ricandmortycomposable.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DetailsScreen(personageId: Int?) {
    Text(text = personageId.toString())
}