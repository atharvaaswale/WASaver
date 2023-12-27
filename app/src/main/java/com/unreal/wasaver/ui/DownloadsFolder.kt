package com.unreal.wasaver.ui

import android.R
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.unreal.wasaver.adapter.DownloadedAdapter
import com.unreal.wasaver.databinding.ActivityDownloadsFolderBinding
import java.io.File


class DownloadsFolder : AppCompatActivity() {
    lateinit var binding: ActivityDownloadsFolderBinding
    var filesList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadsFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.back.setOnClickListener { finish() }
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //window.statusBarColor = Color.TRANSPARENT

        getImagePath() //get status images

        if (filesList.isEmpty()){
            binding.emptyListLayout.visibility = View.VISIBLE
        } else {
            binding.emptyListLayout.visibility = View.GONE
            binding.rv.adapter = DownloadedAdapter(filesList, this)
            binding.rv.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding.rv.setHasFixedSize(true)
        }

    }

    fun getImagePath() {
        val statusDirectory = File(Environment.getExternalStorageDirectory().absolutePath + "/Download/Status/")
        if (statusDirectory.exists() && statusDirectory.isDirectory) {
            val files = statusDirectory.listFiles { file ->
                file.isFile && file.name.endsWith(".jpg")
                        || file.name.endsWith(".jpeg")
                        || file.name.endsWith(".png")
                        || file.name.endsWith(".mp4")
            }
            files?.forEach {
                filesList.add(it.absolutePath)
            }
        } else {
            Toast.makeText(applicationContext, "The directory does not exist or is not a directory.", Toast.LENGTH_SHORT).show()
        }
    }
}