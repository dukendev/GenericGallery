package com.dukendev.genericgallery.presentation.image

import androidx.lifecycle.ViewModel
import com.dukendev.genericgallery.data.model.ImageItem
import com.dukendev.genericgallery.domain.use_case.LocalMediaUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class ImagesViewModel(private val mediaUseCase: LocalMediaUseCase) : ViewModel() {


    var selectedImagePath: MutableStateFlow<ImageItem?> = MutableStateFlow(null)
        private set

    fun updateSelected(image: ImageItem?) {
        if (image == null) {
            return
        }
        selectedImagePath.value = image
    }

    fun getImagesForBucket(bucketId: String) = mediaUseCase.letImagesFlow(bucketId)


}




