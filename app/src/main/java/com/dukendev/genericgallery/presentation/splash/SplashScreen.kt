package com.dukendev.genericgallery.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "Splash Screen")
    }
}