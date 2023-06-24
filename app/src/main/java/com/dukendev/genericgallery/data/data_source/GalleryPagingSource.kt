package com.dukendev.genericgallery.data.data_source

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dukendev.genericgallery.data.model.FolderItem
import org.koin.dsl.koinApplication

class GalleryPagingSource(private val context: Context) : PagingSource<Int, FolderItem>() {



    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FolderItem> {
        return try {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize

            val folders = queryMediaStore(context.contentResolver, currentPage, pageSize)
            val prevKey = if (currentPage > 0) currentPage - 1 else null
            val nextKey = if (folders.size == pageSize) currentPage + 1 else null

            LoadResult.Page(
                data = folders,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun queryMediaStore(
        contentResolver: ContentResolver,
        page: Int,
        pageSize: Int
    ): List<FolderItem> {
        val offset = page * pageSize
        val limit = "$offset, $pageSize"

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME
        )
        val selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ?"
        val selectionArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val folders = mutableListOf<FolderItem>()
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val columnIndexDisplayName =
                it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val columnIndexMimeType = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val columnBucketId = it.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)

            val columnBucketName =
                it.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)

            while (it.moveToNext()) {
                val path = it.getString(columnIndexData)
                val name = it.getString(columnIndexDisplayName)
                val mimeType = it.getString(columnIndexMimeType)
                val bucketId = it.getString(columnBucketId)
                val bucketName = it.getString(columnBucketName)

                val folderItem = FolderItem(path, name, 0, mimeType, bucketName, bucketId)
                folders.add(folderItem)
            }
            Log.d("app", folders.toString())
            return folders.sortedBy {folder -> folder.bucketName }
        }

        return emptyList()
    }

    override fun getRefreshKey(state: PagingState<Int, FolderItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}