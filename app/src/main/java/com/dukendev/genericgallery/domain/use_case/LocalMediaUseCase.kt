package com.dukendev.genericgallery.domain.use_case

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dukendev.genericgallery.data.data_source.GalleryPagingSource
import com.dukendev.genericgallery.data.data_source.ImagePagingSource
import com.dukendev.genericgallery.data.data_source.SearingPagingSource
import com.dukendev.genericgallery.data.model.FolderItem
import com.dukendev.genericgallery.data.model.ImageItem
import kotlinx.coroutines.flow.Flow

class LocalMediaUseCase(private val context: Context) {


    fun letImagesFlow(bucketId: String): Flow<PagingData<ImageItem>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { ImagePagingSource(context, bucketId = bucketId) }
        ).flow
    }

    fun letAlbumsFlow(): Flow<PagingData<FolderItem>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { GalleryPagingSource(context) }
        ).flow
    }


    fun letSearchImagesFlow(name: String): Flow<PagingData<ImageItem>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { SearingPagingSource(context, name = name) }
        ).flow
    }

}