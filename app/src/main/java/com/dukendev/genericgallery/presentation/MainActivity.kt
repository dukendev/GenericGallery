package com.dukendev.genericgallery.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.dukendev.genericgallery.presentation.home.AlbumViewModel
import com.dukendev.genericgallery.presentation.navigation.MainNavHost
import com.dukendev.genericgallery.ui.theme.GenericGalleryTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel


class MainActivity : ComponentActivity() {

    private val readImagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        Manifest.permission.READ_MEDIA_IMAGES
    else
        Manifest.permission.READ_EXTERNAL_STORAGE

    private val isPermissionGranted: MutableStateFlow<Boolean> = MutableStateFlow(false)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = LocalContext.current

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
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            LaunchedEffect(true) {
                checkAndRequestLocationPermissions(
                    context,
                    permissions,
                    launcherMultiplePermissions
                )
            }
            val viewModel = getViewModel<AlbumViewModel>()
            val permissionState =
                rememberPermissionState(permission = readImagePermission)
            GenericGalleryTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                MainNavHost(
                    navController = navController,
                    isPermissionGranted = isPermissionGranted,
                    permissionState = permissionState,
                    checkAndRequestLocationPermissions = {
                        checkAndRequestLocationPermissions(
                            context,
                            permissions = permissions,
                            launcher = launcherMultiplePermissions
                        )
                    },
                    viewModel = viewModel
                )
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
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePermissionScope(
    modifier: Modifier,
    isPermissionGranted: Boolean,
    permissionState: PermissionState,
    requestContent: @Composable () -> Unit,
    body: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
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


