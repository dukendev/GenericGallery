package com.dukendev.genericgallery.presentation.image

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.dukendev.genericgallery.presentation.component.EmptyScreenContent
import com.dukendev.genericgallery.presentation.component.GGTopBar
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

    var searchQuery by remember {
        mutableStateOf("")
    }
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val systemUiController = rememberSystemUiController()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GGTopBar(
                title = "$bucketName",
                hasBack = true,
                scrollBehavior = scrollBehavior,
                hasSearch = true,
                query = searchQuery,
                onNavigate = { navController.navigateUp() },
                onSearchUpdated = {
                    searchQuery = it
                    imagesViewModel.getSearchImages(name = searchQuery, bucketId = bucketId)
                }
            )
        }
    ) { paddingValues ->
        LaunchedEffect(true) {
            systemUiController.setStatusBarColor(Color.Transparent)
            systemUiController.isSystemBarsVisible = false
            systemUiController.isNavigationBarVisible = false
        }

        val searchImages by rememberUpdatedState {
            imagesViewModel.searchImages
        }
        val searchPages = remember(searchQuery) {
            derivedStateOf {
                searchImages
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(targetState = searchQuery.isNotBlank()) { searching ->
                if (searching) {
                    ImagesGrid(
                        images = searchPages.value.invoke().collectAsLazyPagingItems(),
                        onImageSelected = {
                            imagesViewModel.updateSelected(it)
                            navController.navigate(Routes.ImagePreviewScreen.value)
                        })
                } else {
                    val images = remember {
                        imagesViewModel.getImagesForBucket(bucketId ?: "")
                    }.collectAsLazyPagingItems()
                    if (images.itemCount == 0) {
                        EmptyScreenContent()
                    } else {
                        ImagesGrid(images = images, onImageSelected = {
                            imagesViewModel.updateSelected(it)
                            navController.navigate(
                                Routes.ImagePreviewScreen.value
                            )
                        })
                    }
                }
            }
        }
    }
}