package com.unreal.allvideodownloader.model

import android.net.Uri

data class StatusModel(
    val name: String,
    val uri: Uri,
    val path: String,
    val fileName: String,
)
