package com.dukendev.genericgallery.data.model

data class FolderItem(
    val path: String,
    val name: String,
    val count: Int,
    val mimeType: String,
    val bucketName : String,
    val bucketId : String
)