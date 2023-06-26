package com.dukendev.genericgallery.data.data_source

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dukendev.genericgallery.data.model.ImageItem

class SearingPagingSource(private val context: Context, private val name: String) :
    PagingSource<Int, ImageItem>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageItem> {
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
            ).copy(data = folders.filter { imageItem ->
                imageItem.name?.contains(name) == true
            })
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun queryMediaStore(
        contentResolver: ContentResolver,
        page: Int,
        pageSize: Int
    ): List<ImageItem> {
        val offset = page * pageSize
        val limit = "$offset, $pageSize"

        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.RELATIVE_PATH,
            MediaStore.MediaColumns.SIZE
        )
        val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} like?"
        val selectionArgs = arrayOf(
            name
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )




        cursor.use {
            val folders = mutableSetOf<ImageItem>()
            val columnIndexData = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val columnIndexDisplayName =
                it?.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val columnIndexMimeType = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val columnBucketId = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID)
            val columnBucketName =
                it?.getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
            val columnSize = it?.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            if (it != null) {
                while (it.moveToNext()) {
                    val path = columnIndexData?.let { it1 -> it.getString(it1) }
                    val name = columnIndexDisplayName?.let { it1 -> it.getString(it1) }
                    val mimeType = columnIndexMimeType?.let { it1 -> it.getString(it1) }
                    val bucketId = columnBucketId?.let { it1 -> it.getString(it1) }
                    val bucketName = columnBucketName?.let { it1 -> it.getString(it1) }
                    val size = columnSize?.let { it1 -> it.getInt(it1) }
                    val folderItem = ImageItem(
                        bucketId = bucketId,
                        bucketName = bucketName,
                        path = path,
                        name = name,
                        size = size ?: 0,
                        mimeType = mimeType
                    )
                    folders.add(folderItem)
                }
            }
            Log.d("search", folders.toString())
            return folders.sortedBy { folder -> folder.bucketName }
        }

    }

    override fun getRefreshKey(state: PagingState<Int, ImageItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
