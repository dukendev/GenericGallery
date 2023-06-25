package com.dukendev.genericgallery.data.model

data class ImageItem(
    val path: String,
    val name: String,
    val size: Int,
    val mimeType: String,
    val bucketName : String,
    val bucketId : String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageItem

        if (path != other.path) return false
        if (name != other.name) return false
        if (size != other.size) return false
        if (mimeType != other.mimeType) return false
        if (bucketName != other.bucketName) return false
        if (bucketId != other.bucketId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + size
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + bucketName.hashCode()
        result = 31 * result + bucketId.hashCode()
        return result
    }
}