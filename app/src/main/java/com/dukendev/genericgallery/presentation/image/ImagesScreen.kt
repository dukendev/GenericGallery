package com.dukendev.genericgallery.presentation.image

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dukendev.genericgallery.presentation.component.ImagePreview
import com.dukendev.genericgallery.presentation.navigation.Routes

@Composable
fun ImagesScreen(
    navController: NavController,
    bucketId: String?,
    bucketName: String?,
    imagesViewModel: ImagesViewModel
) {

    Box(modifier = Modifier.fillMaxSize()) {
        LaunchedEffect(true) {
            if (bucketId != null) {
                imagesViewModel.letImagesFlow(bucketId)
            }
        }
        val imagesFlow = imagesViewModel.imagesFlow?.collectAsLazyPagingItems()
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(120.dp),
        ) {
            imagesFlow?.itemCount?.let { it1 ->
                items(it1) {
                    imagesFlow[it]?.let { it1 ->
                        ImagePreview(imageItem = it1, modifier = Modifier.clickable {
                            imagesViewModel.updateSelectedPath(it1.path)
                            navController.navigate(
                                Routes.ImagePreviewScreen.value
                            )
                        })
                    }
                }
            }
            when (imagesFlow?.loadState?.append) {
                is LoadState.NotLoading -> Unit
                LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                    Log.d("app", "loading")
                }

                is LoadState.Error -> {
                    item {
                        Text(text = (imagesFlow.loadState.append as LoadState.Error).error.message.toString())
                    }

                    Log.d("app", "error")
                }

                else -> {}
            }
        }
        Text(text = "$bucketId $bucketName")
    }
}