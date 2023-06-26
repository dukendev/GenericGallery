package com.dukendev.genericgallery.presentation.home

import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dukendev.genericgallery.R
import com.dukendev.genericgallery.data.model.ImageItem
import com.dukendev.genericgallery.presentation.component.FolderPreview
import com.dukendev.genericgallery.presentation.component.GGTopBar
import com.dukendev.genericgallery.presentation.component.ImagePermissionScope
import com.dukendev.genericgallery.presentation.image.ImagesGrid
import com.dukendev.genericgallery.presentation.navigation.Routes
import com.dukendev.genericgallery.presentation.navigation.Routes.Companion.navigateWithArgs
import com.dukendev.genericgallery.ui.theme.custom.spacings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AlbumViewModel,
    isPermissionGranted: MutableStateFlow<Boolean>,
    permissionState: PermissionState,
    navController: NavHostController,
    checkAndRequestLocationPermissions: () -> Unit,
    onSearched: (ImageItem) -> Unit
) {


    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val isGranted by isPermissionGranted.collectAsState()
    var searchQuery by remember {
        mutableStateOf("")
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GGTopBar(
                title = "Photo Albums",
                scrollBehavior = scrollBehavior,
                hasSearch = true,
                query = searchQuery,
                onSearchUpdated = {
                    searchQuery = it
                    viewModel.getSearchImages(name = searchQuery, null)
                })
        }) { paddingValues ->
        ImagePermissionScope(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isPermissionGranted = isGranted,
            permissionState = permissionState,
            requestContent = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Permission is required")
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionState.launchPermissionRequest()
                        } else {
                            checkAndRequestLocationPermissions()
                        }
                    }) {
                        Text(text = "Allow")
                    }
                }
            }) {
            val systemUiController = rememberSystemUiController()
            LaunchedEffect(true) {
                systemUiController.setStatusBarColor(Color.Transparent)
                systemUiController.isSystemBarsVisible = false
                systemUiController.isNavigationBarVisible = false
            }
            val albumsFlow = remember {
                viewModel.getAlbums()
            }.collectAsLazyPagingItems()

            val searchImages by rememberUpdatedState {
                viewModel.searchImages
            }
            val images = remember(searchQuery) {
                derivedStateOf {
                    searchImages
                }
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AnimatedContent(targetState = searchQuery.isNotBlank()) { searching ->
                    if (searching) {
                        ImagesGrid(
                            images = images.value.invoke().collectAsLazyPagingItems(),
                            onImageSelected = {
                                onSearched(it)
                                navController.navigate(Routes.ImagePreviewScreen.value)
                            })
                    } else {
                        if (albumsFlow.itemCount == 0) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.folder_empty),
                                        contentDescription = null
                                    )
                                    Text(text = "No Folders Found")
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                modifier = Modifier.fillMaxWidth(),
                                columns = GridCells.Adaptive(180.dp),
                                contentPadding = PaddingValues(MaterialTheme.spacings.small),
                                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium)
                            ) {
                                items(albumsFlow.itemCount) { index ->
                                    albumsFlow[index]?.let { folder ->
                                        FolderPreview(folderItem = folder, onItemClick = {
                                            navController.navigate(
                                                Routes.AlbumDetailsScreen.navigateWithArgs(
                                                    bucketId = albumsFlow[index]?.bucketId ?: "",
                                                    albumsFlow[index]?.bucketName ?: ""
                                                )
                                            )
                                        })
                                    }
                                }
                                when (albumsFlow.loadState.append) {
                                    is LoadState.NotLoading -> Unit
                                    LoadState.Loading -> {
                                        item { CircularProgressIndicator() }
                                        Log.d("app", "loading")
                                    }

                                    is LoadState.Error -> {
                                        item {
                                            Text(text = (albumsFlow.loadState.append as LoadState.Error).error.message.toString())
                                        }
                                        Log.d("app", "error")
                                    }

                                    else -> {
                                        item { CircularProgressIndicator() }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }

    }
}