package com.dukendev.genericgallery.presentation.navigation

sealed class Routes(val value: String) {
    object AlbumScreen : Routes("album_screen_route")
    object AlbumDetailsScreen :
        Routes("album_details_screen_route/{bucketId}/?bucketName={bucketName}")

    object ImagePreviewScreen : Routes("image_preview_screen_route")

    companion object {
        fun AlbumDetailsScreen.navigateWithArgs(
            bucketId: String, bucketName: String
        ): String {
            return this.value.replace(
                oldValue = "{bucketId}", newValue = bucketId
            ).replace(
                oldValue = "{bucketName}", newValue = bucketName
            )
        }


        fun ImagePreviewScreen.navigateWithArgs(
            path: String
        ): String {
            return this.value.replace(
                oldValue = "{path}", newValue = path
            )
        }
    }
}