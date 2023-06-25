package com.dukendev.genericgallery.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dukendev.genericgallery.R
import com.dukendev.genericgallery.data.model.FolderItem
import com.dukendev.genericgallery.ui.theme.custom.extendedShape
import com.dukendev.genericgallery.ui.theme.custom.spacings

@Composable
fun FolderPreview(folderItem: FolderItem) {
    Box(modifier = Modifier.wrapContentSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.scrim,
                    shape = MaterialTheme.shapes.medium
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = if (folderItem.data.isNotEmpty()) rememberAsyncImagePainter(model = folderItem.data) else painterResource(
                    id = R.drawable.folder_empty
                ),
                contentDescription = null,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.extendedShape.topEndRoundedCorner),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .height(intrinsicSize = IntrinsicSize.Min)
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacings.medium),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = folderItem.bucketName,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = folderItem.relativePath,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Visible
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacings.extraSmall))
            }

        }
    }
}