package com.dukendev.genericgallery.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dukendev.genericgallery.domain.use_case.LocalMediaUseCase

class AlbumViewModel(private val mediaUseCase: LocalMediaUseCase) : ViewModel() {

    fun getAlbums() = mediaUseCase.letAlbumsFlow().cachedIn(viewModelScope)


}




