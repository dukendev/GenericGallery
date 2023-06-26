package com.dukendev.genericgallery.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dukendev.genericgallery.data.model.ImageItem
import com.dukendev.genericgallery.domain.use_case.LocalMediaUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class AlbumViewModel(private val mediaUseCase: LocalMediaUseCase) : ViewModel() {
    var searchImages: Flow<PagingData<ImageItem>> = flowOf()
        private set

    fun getAlbums() = mediaUseCase.letAlbumsFlow().cachedIn(viewModelScope)

    fun getSearchImages(name: String, bucketId: String?) {
        searchImages = mediaUseCase.letSearchImagesFlow(name = name, bucketId = bucketId)
    }

}





