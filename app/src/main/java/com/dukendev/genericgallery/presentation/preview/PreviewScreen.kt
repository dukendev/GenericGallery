package com.dukendev.genericgallery.presentation.preview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.dukendev.genericgallery.R
import com.dukendev.genericgallery.presentation.component.GGTopBar
import com.dukendev.genericgallery.presentation.component.ZoomableImage
import com.dukendev.genericgallery.presentation.image.ImagesViewModel
import com.dukendev.genericgallery.ui.theme.custom.spacings
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(navController: NavController, imagesViewModel: ImagesViewModel) {

    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(true) {
        systemUiController.setStatusBarColor(Color.Transparent)
        systemUiController.isSystemBarsVisible = false
        systemUiController.isNavigationBarVisible = true
    }
    val image by imagesViewModel.selectedImagePath.collectAsStateWithLifecycle()
    var isUiRevealed by remember {
        mutableStateOf(true)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar =
        {
            AnimatedVisibility(isUiRevealed) {
                GGTopBar(
                    title = "${image?.name}",
                    hasBack = true,
                    scrollBehavior = scrollBehavior
                ) {
                    navController.navigateUp()
                }
            }
        }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .clickable {
                    isUiRevealed = !isUiRevealed
                }
        ) {
            ZoomableImage(
                painter = if (image != null) rememberAsyncImagePainter(model = image?.path) else painterResource(
                    id = R.drawable.folder_empty
                )
            )
            AnimatedVisibility(
                modifier = Modifier
                    .height(420.dp)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .align(Alignment.BottomCenter), visible = isUiRevealed
            ) {
                Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "name : ${image?.name}",
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacings.small)
                    )

                    Text(
                        text = "size : ${image?.size?.div(1024L)}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacings.small)
                    )

                    Text(
                        text = "location : ${image?.path}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacings.small)
                    )
                }
            }
        }
    }
}

