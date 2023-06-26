package com.dukendev.genericgallery.di

import com.dukendev.genericgallery.domain.use_case.LocalMediaUseCase
import com.dukendev.genericgallery.presentation.home.AlbumViewModel
import com.dukendev.genericgallery.presentation.image.ImagesViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        LocalMediaUseCase(androidContext())
    }

    viewModel {
        ImagesViewModel(get())
    }

    viewModel {
        AlbumViewModel(get())
    }


}