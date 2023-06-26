package com.dukendev.genericgallery.presentation.component

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState

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
