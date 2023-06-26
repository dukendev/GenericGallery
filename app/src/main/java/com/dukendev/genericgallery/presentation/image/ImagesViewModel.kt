package com.dukendev.genericgallery.presentation.image

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.dukendev.genericgallery.data.model.ImageItem
import com.dukendev.genericgallery.domain.use_case.LocalMediaUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class ImagesViewModel(private val mediaUseCase: LocalMediaUseCase) : ViewModel() {
    var searchImages: Flow<PagingData<ImageItem>> = flowOf()
        private set
    var selectedImagePath: MutableStateFlow<ImageItem?> = MutableStateFlow(null)
        private set

    fun updateSelected(image: ImageItem?) {
        if (image == null) {
            return
        }
        selectedImagePath.value = image
    }

    fun getImagesForBucket(bucketId: String) = mediaUseCase.letImagesFlow(bucketId)

    fun getSearchImages(name: String, bucketId: String?) {
        searchImages = mediaUseCase.letSearchImagesFlow(name = name, bucketId = bucketId)
    }

}




