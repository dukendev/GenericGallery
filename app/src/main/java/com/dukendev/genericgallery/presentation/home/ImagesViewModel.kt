package com.dukendev.genericgallery.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dukendev.genericgallery.data.data_source.GalleryPagingSource
import com.dukendev.genericgallery.data.model.FolderItem
import kotlinx.coroutines.flow.Flow

class ImagesViewModel(private val context: Context) : ViewModel() {


    val imagesFlow: Flow<PagingData<FolderItem>> = Pager(
        config = PagingConfig(pageSize = 30),
        pagingSourceFactory = { GalleryPagingSource(context) }
    ).flow


}




