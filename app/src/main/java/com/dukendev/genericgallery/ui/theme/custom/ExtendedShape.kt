package com.dukendev.genericgallery.ui.theme.custom

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

data class ExtendedShape(
	val topEndSmallRoundedCorner: RoundedCornerShape = RoundedCornerShape(
		topStart = 8.dp,
		topEnd = 8.dp
	),
	val topEndRoundedCorner: RoundedCornerShape = RoundedCornerShape(
		topStart = 16.dp,
		topEnd = 16.dp
	),
	val topEndLargeRoundedCorner: RoundedCornerShape = RoundedCornerShape(
		topStart = 32.dp,
		topEnd = 32.dp
	),
	val veryLargeRoundedCorner: RoundedCornerShape = RoundedCornerShape(32.dp),
	val bottomEndLargeRoundedCorner: RoundedCornerShape = RoundedCornerShape(
		topStart = 32.dp,
		topEnd = 32.dp
	),
)

val LocalExtendedShape = compositionLocalOf { ExtendedShape() }

val MaterialTheme.extendedShape: ExtendedShape
	@Composable
	@ReadOnlyComposable
	get() = LocalExtendedShape.current