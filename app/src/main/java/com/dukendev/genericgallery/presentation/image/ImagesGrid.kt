package com.dukendev.genericgallery.presentation.image

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.dukendev.genericgallery.data.model.ImageItem
import com.dukendev.genericgallery.presentation.component.ImagePreview

@Composable
fun ImagesGrid(images: LazyPagingItems<ImageItem>, onImageSelected: (ImageItem) -> Unit) {

    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth(),
        columns = GridCells.Adaptive(120.dp),
    ) {
        items(images.itemCount) { index ->
            images[index]?.let { image ->
                ImagePreview(imageItem = image, modifier = Modifier.clickable {
                    onImageSelected(image)
                })
            }
        }
        when (images.loadState.append) {
            is LoadState.NotLoading -> Unit
            LoadState.Loading -> {
                item { CircularProgressIndicator() }
                Log.d("app", "loading")
            }

            is LoadState.Error -> {
                item {
                    Text(text = (images.loadState.append as LoadState.Error).error.message.toString())
                }
                Log.d("app", "error")
            }
            else -> {}
        }
    }
}