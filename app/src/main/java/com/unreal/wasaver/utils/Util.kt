package com.unreal.allvideodownloader.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

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

        fun savefile(sourceuri: String, context: Context) {
            val inpstrm: InputStream? = context.contentResolver.openInputStream(
                Uri.fromFile(File(sourceuri))
            )
            val fileName = Uri.parse(sourceuri).path
            var f: File? = null
            val extention = sourceuri.substring(sourceuri.lastIndexOf("."))
            f = File(
                Environment.getExternalStorageDirectory().absolutePath +
                        "/Download/Status/" +
                        System.currentTimeMillis() +
                        extention
            )
            f!!.setWritable(true, false)
            val outputStream: OutputStream = FileOutputStream(f)
            val buffer = ByteArray(1024)
            var length = 0
            if (inpstrm != null) {
                while (inpstrm.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
            }
            inpstrm!!.close()
            outputStream.close()
            Toast.makeText(context, "Download completed", Toast.LENGTH_SHORT).show()
            inpstrm!!.close()
        }

    }
    

}