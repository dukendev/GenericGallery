package com.dukendev.genericgallery.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.dukendev.genericgallery.data.model.FolderItem
import com.dukendev.genericgallery.presentation.home.ImagesViewModel
import com.dukendev.genericgallery.ui.theme.GenericGalleryTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE


    private var list: MutableList<FolderItem> = mutableListOf()


    private val isPermissionGranted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imagesViewModel by viewModel<ImagesViewModel>()
//        list = queryMediaStore(contentResolver,1,30) as MutableList<FolderItem>
        setContent {
            GenericGalleryTheme {
                // A surface container using the 'background' color from the theme
                val context = LocalContext.current
                val permissionState =
                    rememberPermissionState(permission = readImagePermission)

                val openDialog = remember { mutableStateOf(false) }
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                val launcherMultiplePermissions = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissionsMap ->
                    val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
                    if (areGranted) {
                        // Use location
                        isPermissionGranted.value = true
                    } else {
                        // Show dialog
                        openDialog.value = true

                    }
                }
                LaunchedEffect(true) {
                    checkAndRequestLocationPermissions(
                        context,
                        permissions,
                        launcherMultiplePermissions
                    )
                }
                val imagesFlow = imagesViewModel.imagesFlow.collectAsLazyPagingItems()

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onDismissRequest.
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Title")
                        },
                        text = {
                            Text(text = "Turned on by default")
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    openDialog.value = false
                                }
                            ) {
                                Text("Dismiss")
                            }
                        }
                    )
                }


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

                                    checkAndRequestLocationPermissions(
                                        context,
                                        permissions,
                                        launcherMultiplePermissions
                                    )


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
                        LazyColumn {
                            items(imagesFlow.itemCount) {
                                imagesFlow[it]?.name?.let { it1 ->
                                    Text(
                                        text = "$it1 from folder ${
                                            imagesFlow[it]?.bucketId
                                        }"
                                    )
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
    }
    //end of onCreate


    private fun checkAndRequestLocationPermissions(
        context: Context,
        permissions: Array<String>,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        if (
            permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            // Use location because permissions are already granted
            isPermissionGranted.value = true
        } else {
            // Request permissions
            launcher.launch(permissions)
        }
    }

//    private fun queryMediaStore(
//        contentResolver: ContentResolver,
//        page: Int,
//        pageSize: Int
//    ): List<FolderItem> {
//        val offset = page * pageSize
//        val limit = "$offset, $pageSize"
//
//        val projection = arrayOf(
//            MediaStore.MediaColumns.DATA,
//            MediaStore.MediaColumns.DISPLAY_NAME,
//            MediaStore.MediaColumns.MIME_TYPE,
//        )
//        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
//        val selectionArgs = arrayOf(
//            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
//        )
//        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
//
//        val cursor = contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            null,
//            null,
//            null
//        )
//
//        cursor?.use {
//            val folders = mutableListOf<FolderItem>()
//            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
//            val columnIndexDisplayName = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
//            val columnIndexMimeType = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
//
//            while (it.moveToNext()) {
//                val path = it.getString(columnIndexData)
//                val name = it.getString(columnIndexDisplayName)
//                val mimeType = it.getString(columnIndexMimeType)
//
//                val folderItem = FolderItem(path, name, 0, mimeType)
//                folders.add(folderItem)
//            }
//            Log.d("app",folders.toString())
//            return folders
//        }
//
//        return emptyList()
//    }


}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePermissionScope(
    readImagePermission: String,
    isPermissionGranted: Boolean,
    permissionState: PermissionState,
    requestContent: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            PermissionRequired(
                permissionState = permissionState,
                permissionNotGrantedContent = {
                    requestContent()
                },
                permissionNotAvailableContent = {
                    requestContent()
                }) {
                body()
            }
        } else {
            if (isPermissionGranted) {
                body()
            } else {
                requestContent()
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GenericGalleryTheme {
        Greeting("Android")
    }
}