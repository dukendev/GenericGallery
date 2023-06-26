package com.dukendev.genericgallery.presentation.image

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dukendev.genericgallery.presentation.component.GGTopBar
import com.dukendev.genericgallery.presentation.component.ImagePreview
import com.dukendev.genericgallery.presentation.navigation.Routes
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
    navController: NavController,
    bucketId: String?,
    bucketName: String?,
    imagesViewModel: ImagesViewModel
) {

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val systemUiController = rememberSystemUiController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GGTopBar(title = "$bucketName", hasBack = true, scrollBehavior = scrollBehavior) {
                navController.navigateUp()
            }

        }) { paddingValues ->
        LaunchedEffect(true) {
            systemUiController.setStatusBarColor(Color.Transparent)
            systemUiController.isSystemBarsVisible = false
            systemUiController.isNavigationBarVisible = false
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                imagesFlow?.itemCount?.let { count ->
                    items(count) { index ->
                        imagesFlow[index]?.let { image ->
                            ImagePreview(imageItem = image, modifier = Modifier.clickable {
                                imagesViewModel.updateSelected(image)
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
        }
    }
}