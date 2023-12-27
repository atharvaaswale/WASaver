package com.unreal.wasaver.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.unreal.allvideodownloader.utils.Util
import com.unreal.wasaver.R
import com.unreal.wasaver.databinding.PreviewBinding
import java.io.File

class CustomFullScreenDialog(
    var appContext: Context,
    private val activity: Activity,
    var data: String
) : Dialog(appContext) {
    lateinit var binding: PreviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.preview,
            null,
            false
        )
        setContentView(binding.root)
        window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        initUI()
    }

    private fun initUI() {
        Glide.with(appContext).load(data).into(binding.img)
        binding.close.setOnClickListener { dismiss() }
        binding.send.setOnClickListener{
            val appContext = this
            val imagePath = data

            val imageFile = File(imagePath)
            if (imageFile.exists()) {
                val imageUri = Uri.fromFile(imageFile)

                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.type = "image/*"
                sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject for the image")
                //sendIntent.putExtra(Intent.EXTRA_TEXT, "Additional message for sharing")

                activity.startActivity(Intent.createChooser(sendIntent, "Share Image"))
            } else {
                // Handle the case where the image file does not exist
            }

        }
        binding.save.setOnClickListener {
            Util.savefile(data, appContext)
        }
    }
}
