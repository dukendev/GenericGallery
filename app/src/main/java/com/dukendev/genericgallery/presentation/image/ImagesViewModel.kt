package com.dukendev.genericgallery.presentation.image

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dukendev.genericgallery.data.data_source.ImagePagingSource
import com.dukendev.genericgallery.data.model.ImageItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ImagesViewModel(private val context: Context) : ViewModel() {

    var imagesFlow: Flow<PagingData<ImageItem>>? = null
        private set

    var selectedImagePath: MutableStateFlow<String> = MutableStateFlow("")
        private set

    fun updateSelectedPath(path: String?) {
        if (path == null) {
            return
        }
        selectedImagePath.value = path
    }

    fun letImagesFlow(bucketId: String) {
        imagesFlow = Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { ImagePagingSource(context, bucketId = bucketId) }
        ).flow
    }
}




