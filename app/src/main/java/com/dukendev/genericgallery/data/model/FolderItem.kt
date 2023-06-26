package com.dukendev.genericgallery.data.model

data class FolderItem(
    val bucketId : String?,
    val bucketName : String?,
    val relativePath : String?,
    val data : String?
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FolderItem

        if (bucketId != other.bucketId) return false
        if (bucketName != other.bucketName) return false
        if (relativePath != other.relativePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bucketId.hashCode()
        result = 31 * result + bucketName.hashCode()
        result = 31 * result + relativePath.hashCode()
        return result
    }
}

