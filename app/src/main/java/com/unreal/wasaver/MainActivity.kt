package com.unreal.wasaver

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.unreal.wasaver.databinding.ActivityMainBinding
import com.unreal.wasaver.ui.DownloadsFolder
import com.unreal.wasaver.ui.NewActivity
import com.unreal.wasaver.ui.WhatsappStatusSaver

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isAllPermissionGranted()
        initUI()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initUI() {
        binding.status.setOnClickListener {
            //if (isAllPermissionGranted()){
                val intent = Intent(this, WhatsappStatusSaver::class.java)
                startActivity(intent)
            //}

        }
        binding.downloads.setOnClickListener {
            //if (isAllPermissionGranted()) {
                val intent = Intent(this, DownloadsFolder::class.java)
                startActivity(intent)
            //}
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun isAllPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                84
            )
            false
        } else { true }
    }

}