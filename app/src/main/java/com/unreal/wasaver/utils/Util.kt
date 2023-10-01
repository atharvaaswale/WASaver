package com.unreal.allvideodownloader.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import java.io.File

class Util {
    companion object{
        var rootDirectoryWhatsapp: File = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/SavedStatus")
        var WHATSAPP_STATUS_FOLDER_PATH = "/WhatsApp/Media/.Statuses/"

        fun isImageFile(context: Context, path: String): Boolean {
            val uri: Uri = Uri.parse(path)

            val mimeType: String?
            mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                val cr = context.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase())
            }


            return mimeType != null && mimeType.startsWith("image")
        }

        fun isVideoFile(context: Context,path: String): Boolean {

            val uri:Uri = Uri.parse(path)

            val mimeType: String?
            mimeType = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
                val cr = context.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString())
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase())
            }

            return mimeType != null && mimeType.startsWith("video")
        }
    }
    

}