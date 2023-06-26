package com.dukendev.genericgallery.presentation.component


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dukendev.genericgallery.R
import com.dukendev.genericgallery.data.model.ImageItem
import com.dukendev.genericgallery.ui.theme.custom.spacings

@Composable
fun ImagePreview(modifier: Modifier, imageItem: ImageItem) {
    Box(modifier = modifier.wrapContentSize()) {
        Column(
            Modifier
                .fillMaxSize(),

            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = if (imageItem.path?.isNotEmpty() == true) rememberAsyncImagePainter(model = imageItem.path) else painterResource(
                    id = R.drawable.folder_empty
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .height(intrinsicSize = IntrinsicSize.Min)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(MaterialTheme.spacings.small),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = imageItem.name ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(Color.White),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${imageItem.size.div(1024L)} Kb",
                    style = MaterialTheme.typography.labelSmall.copy(Color.White),
                    maxLines = 2,
                    overflow = TextOverflow.Visible
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacings.extraSmall))
            }

        }
    }
}
