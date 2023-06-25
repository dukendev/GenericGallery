package com.dukendev.genericgallery.di

import com.dukendev.genericgallery.presentation.home.AlbumViewModel
import com.dukendev.genericgallery.presentation.image.ImagesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        ImagesViewModel(androidContext())
    }

    viewModel {
        AlbumViewModel(androidContext())
    }
}