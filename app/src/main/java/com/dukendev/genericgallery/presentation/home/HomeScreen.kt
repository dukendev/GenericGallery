package com.dukendev.genericgallery.presentation.home

import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dukendev.genericgallery.R
import com.dukendev.genericgallery.presentation.ImagePermissionScope
import com.dukendev.genericgallery.presentation.component.FolderPreview
import com.dukendev.genericgallery.presentation.navigation.Routes
import com.dukendev.genericgallery.presentation.navigation.Routes.Companion.navigateWithArgs
import com.dukendev.genericgallery.ui.theme.custom.spacings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    readImagePermission: String,
    isPermissionGranted: MutableStateFlow<Boolean>,
    permissionState: PermissionState,
    navController: NavHostController,
    checkAndRequestLocationPermissions: () -> Unit,
    viewModel: AlbumViewModel
) {
    val imagesFlow = viewModel.albumsFlow.collectAsLazyPagingItems()
    val isGranted by isPermissionGranted.collectAsState()
    ImagePermissionScope(
        readImagePermission = readImagePermission,
        isPermissionGranted = isGranted,
        permissionState = permissionState,
        requestContent = {
            Box(modifier = Modifier.fillMaxSize()) {
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
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (imagesFlow.itemCount == 0) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
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
                    items(imagesFlow.itemCount) {
                        imagesFlow[it]?.let { it1 ->
                            FolderPreview(folderItem = it1, modifier = Modifier.clickable {
                                navController.navigate(
                                    Routes.AlbumDetailsScreen.navigateWithArgs(
                                        bucketId = imagesFlow[it]?.bucketId ?: "",
                                        imagesFlow[it]?.bucketName ?: ""
                                    )
                                )
                            })
                        }
                    }
                    when (imagesFlow.loadState.append) {
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
                    }
                }
            }

        }
    }
}