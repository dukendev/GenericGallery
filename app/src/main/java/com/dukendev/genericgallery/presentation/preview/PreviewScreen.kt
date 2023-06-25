package com.dukendev.genericgallery.presentation.preview

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.dukendev.genericgallery.R
import com.dukendev.genericgallery.presentation.image.ImagesViewModel

@Composable
fun PreviewScreen(imagesViewModel: ImagesViewModel) {

    val path by imagesViewModel.selectedImagePath.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = if (path.isNotEmpty()) rememberAsyncImagePainter(model = path) else painterResource(
                id = R.drawable.folder_empty
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
    }

}