package com.dukendev.genericgallery.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GGTopBar(
    title: String,
    hasBack: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigate: () -> Unit = {}
) {
    MediumTopAppBar(title = {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }, navigationIcon = {
        AnimatedVisibility(hasBack, enter = expandIn(), exit = shrinkOut()) {
            IconButton(onClick = { onNavigate() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    }, scrollBehavior = scrollBehavior)
}