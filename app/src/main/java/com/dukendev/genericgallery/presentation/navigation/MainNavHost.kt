package com.dukendev.genericgallery.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.dukendev.genericgallery.presentation.component.composableWithDefaultTransition
import com.dukendev.genericgallery.presentation.home.AlbumViewModel
import com.dukendev.genericgallery.presentation.home.HomeScreen
import com.dukendev.genericgallery.presentation.image.ImagesScreen
import com.dukendev.genericgallery.presentation.image.ImagesViewModel
import com.dukendev.genericgallery.presentation.preview.PreviewScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainNavHost(
    isPermissionGranted: MutableStateFlow<Boolean>,
    permissionState: PermissionState,
    navController: NavHostController,
    checkAndRequestLocationPermissions: () -> Unit,
    viewModel: AlbumViewModel
) {

    val imagesViewModel: ImagesViewModel = getViewModel()
    NavHost(navController = navController, startDestination = Routes.AlbumScreen.value) {

        composableWithDefaultTransition(route = Routes.AlbumScreen.value) {
            HomeScreen(
                isPermissionGranted = isPermissionGranted,
                permissionState = permissionState,
                navController = navController,
                checkAndRequestLocationPermissions = checkAndRequestLocationPermissions,
                viewModel = viewModel
            )

        }
        composableWithDefaultTransition(route = Routes.AlbumDetailsScreen.value) {
            val bucketId = it.arguments?.getString("bucketId")
            val bucketName = it.arguments?.getString("bucketName")

            ImagesScreen(
                navController = navController,
                bucketId = bucketId,
                bucketName = bucketName,
                imagesViewModel = imagesViewModel
            )
        }
        composableWithDefaultTransition(route = Routes.ImagePreviewScreen.value) {
            PreviewScreen(navController = navController, imagesViewModel = imagesViewModel)
        }
    }

}