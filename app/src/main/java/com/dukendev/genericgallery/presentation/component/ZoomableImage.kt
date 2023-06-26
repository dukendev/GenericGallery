package com.dukendev.genericgallery.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale

@Composable
fun ZoomableImage(painter: Painter) {
    val scale = remember { mutableFloatStateOf(1f) }
    val rotationState = remember { mutableFloatStateOf(0f) }
    val panXState = remember { mutableFloatStateOf(0f) }
    val panYState = remember { mutableFloatStateOf(0f) }
    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, rotation ->
                    scale.floatValue *= zoom
                    rotationState.floatValue += rotation
                    panXState.floatValue += pan.x
                    panYState.floatValue += pan.y
                }
            }
    ) {
        Image(
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    // adding some zoom limits (min 50%, max 200%)
                    scaleX = maxOf(.5f, minOf(3f, scale.floatValue)),
                    scaleY = maxOf(.5f, minOf(3f, scale.floatValue)),
                    rotationZ = rotationState.floatValue,
                    translationX = panXState.floatValue,
                    translationY = panYState.floatValue
                ),
            contentDescription = null,
            painter = painter
        )
    }
}
