package com.dukendev.genericgallery.data.data_source

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dukendev.genericgallery.data.model.FolderItem

class GalleryPagingSource(private val context: Context) : PagingSource<Int, FolderItem>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FolderItem> {
        return try {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize

            val folders = queryMediaStore(context.contentResolver)
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
    ): List<FolderItem> {

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.RELATIVE_PATH
        )
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor.use {
            val folders = mutableSetOf<FolderItem>()
            val columnIndexData = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val columnIndexDisplayName =
                it?.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val columnIndexMimeType = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val columnBucketId = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)

            val columnBucketName =
                it?.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)

            val columnRelPath =
                it?.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH)
            if (it != null) {
                while (it.moveToNext()) {
                    val path = columnIndexData?.let { it1 -> it.getString(it1) }
                    val name = columnIndexDisplayName?.let { it1 -> it.getString(it1) }
                    val mimeType = columnIndexMimeType?.let { it1 -> it.getString(it1) }
                    val bucketId = columnBucketId?.let { it1 -> it.getString(it1) }
                    val bucketName = columnBucketName?.let { it1 -> it.getString(it1) }

                    val relativePath = columnRelPath?.let { it1 -> it.getString(it1) }

                    val folderItem = FolderItem(
                        bucketId = bucketId,
                        bucketName = bucketName,
                        relativePath = relativePath,
                        data = path
                    )
                    folders.add(folderItem)
                }
            }
            Log.d("app", folders.toString())
            return folders.sortedBy { folder -> folder.bucketName }
        }

    }

    override fun getRefreshKey(state: PagingState<Int, FolderItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}