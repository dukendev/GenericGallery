package com.dukendev.genericgallery.presentation.navigation

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dukendev.genericgallery.presentation.component.composableWithDefaultTransition
import com.dukendev.genericgallery.presentation.component.composableWithFadeTransition
import com.dukendev.genericgallery.presentation.splash.SplashScreen
import com.google.accompanist.permissions.rememberPermissionState

@Composable
fun MainNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.SplashScreen.value) {
        composableWithFadeTransition(route = Routes.SplashScreen.value) {
            SplashScreen()
        }
        composableWithDefaultTransition(route = Routes.AlbumScreen.value) {
//                val context = LocalContext.current
//                val permissionState =
//                    rememberPermissionState(permission = readImagePermission)
//
//                val openDialog = remember { mutableStateOf(false) }
//                val permissions = arrayOf(
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//                val launcherMultiplePermissions = rememberLauncherForActivityResult(
//                    ActivityResultContracts.RequestMultiplePermissions()
//                ) { permissionsMap ->
//                    val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
//                    if (areGranted) {
//                        // Use location
//                        isPermissionGranted.value = true
//                    } else {
//                        // Show dialog
//                        openDialog.value = true
//
//                    }
//                }
//                LaunchedEffect(true) {
//                    checkAndRequestLocationPermissions(
//                        context,
//                        permissions,
//                        launcherMultiplePermissions
//                    )
//                }
//                val imagesFlow = imagesViewModel.imagesFlow.collectAsLazyPagingItems()
//
//                if (openDialog.value) {
//                    AlertDialog(
//                        onDismissRequest = {
//                            // Dismiss the dialog when the user clicks outside the dialog or on the back
//                            // button. If you want to disable that functionality, simply use an empty
//                            // onDismissRequest.
//                            openDialog.value = false
//                        },
//                        title = {
//                            Text(text = "Title")
//                        },
//                        text = {
//                            Text(text = "Turned on by default")
//                        },
//                        confirmButton = {
//                            TextButton(
//                                onClick = {
//                                    openDialog.value = false
//                                }
//                            ) {
//                                Text("Confirm")
//                            }
//                        },
//                        dismissButton = {
//                            TextButton(
//                                onClick = {
//                                    openDialog.value = false
//                                }
//                            ) {
//                                Text("Dismiss")
//                            }
//                        }
//                    )
//                }
//
//
//                val isGranted by isPermissionGranted.collectAsState()
//                ImagePermissionScope(
//                    readImagePermission = readImagePermission,
//                    isPermissionGranted = isGranted,
//                    permissionState = permissionState,
//                    requestContent = {
//                        Box(modifier = Modifier.fillMaxSize()) {
//                            Text(text = "Permission is required")
//                            Button(onClick = {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                                    permissionState.launchPermissionRequest()
//                                } else {
//
//                                    checkAndRequestLocationPermissions(
//                                        context,
//                                        permissions,
//                                        launcherMultiplePermissions
//                                    )
//
//
//                                }
//                            }) {
//                                Text(text = "Allow")
//                            }
//                        }
//                    }) {
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.background
//                    ) {
//
//                        LazyVerticalGrid(
//                            modifier = Modifier.fillMaxWidth(),
//                            columns = GridCells.Adaptive(180.dp),
//                            contentPadding = PaddingValues(MaterialTheme.spacings.small),
//                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium),
//                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacings.medium)
//                        ) {
//                            items(imagesFlow.itemCount) {
//                                imagesFlow[it]?.let { it1 ->
//                                    FolderPreview(folderItem = it1)
//                                }
//                            }
//                            when (imagesFlow.loadState.append) {
//                                is LoadState.NotLoading -> Unit
//                                LoadState.Loading -> {
//                                    item { CircularProgressIndicator() }
//                                    Log.d("app", "loading")
//                                }
//
//                                is LoadState.Error -> {
//                                    item {
//                                        Text(text = (imagesFlow.loadState.append as LoadState.Error).error.message.toString())
//                                    }
//
//                                    Log.d("app", "error")
//                                }
//                            }
//                        }
//                    }
//                }

        }
        composableWithDefaultTransition(route = Routes.AlbumDetailsScreen.value) {

        }
        composableWithDefaultTransition(route = Routes.ImagePreviewScreen.value) {

        }
    }

}