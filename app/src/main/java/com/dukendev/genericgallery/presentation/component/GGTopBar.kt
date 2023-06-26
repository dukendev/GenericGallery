package com.dukendev.genericgallery.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GGTopBar(
    title: String,
    hasBack: Boolean = false,
    scrollBehavior: TopAppBarScrollBehavior,
    hasSearch: Boolean = false,
    query: String = "",
    onSearchUpdated: (String) -> Unit = { _ -> },
    onNavigate: () -> Unit = {}
) {
    var isSearching by remember {
        mutableStateOf(false)
    }
    MediumTopAppBar(title = {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
    }, navigationIcon = {
        AnimatedVisibility(hasBack, enter = expandIn(), exit = shrinkOut()) {
            IconButton(onClick = { onNavigate() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    }, actions = {
        if (hasSearch) {
            Row {
                AnimatedVisibility(visible = isSearching) {
                    TextField(value = query, onValueChange = {
                        onSearchUpdated(it)
                    })
                }

                IconButton(onClick = {
                    isSearching = !isSearching
                }) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            }
        }
    }, scrollBehavior = scrollBehavior)
}