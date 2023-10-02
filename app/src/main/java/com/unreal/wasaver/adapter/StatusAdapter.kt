package com.unreal.wasaver.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.AnimRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.unreal.wasaver.R
import java.io.*


class StatusAdapter(
    private val statusList: ArrayList<String>,
    val context: Context,
) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.whatsapp_status_column, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(statusList[position]).into(holder.image)
        holder.download.setOnClickListener {
            savefile(statusList[position])
        }
        holder.card.setOnLongClickListener(object : OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                showPreview(statusList[position])
                return true
            }

        })
        if (statusList[position].endsWith(".mp4")) {
            holder.play.visibility = View.VISIBLE
            holder.image.setOnClickListener {
                val dialog = Dialog(holder.image.context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.video_dialog)
                val download = dialog.findViewById<Button>(R.id.dwVideo)
                val player = dialog.findViewById<VideoView>(R.id.videoPlayer)
                player.setVideoURI(Uri.parse(statusList[position]))
                val mediaController = MediaController(holder.image.context)
                mediaController.setAnchorView(player)
                mediaController.setMediaPlayer(player)
                player.setMediaController(mediaController)
                player.start()
                val fileName = dialog.findViewById<TextView>(R.id.fileName)
                val Size = dialog.findViewById<TextView>(R.id.fileSize)
                val extention =
                    statusList[position].substring(statusList[position].lastIndexOf("."))
                val fileDescriptor = context.contentResolver
                    .openAssetFileDescriptor(Uri.fromFile(File(statusList[position])), "r")
                val fileSize = fileDescriptor!!.length / 1024 / 1024
                fileName.text = "${System.currentTimeMillis()}$extention"
                Size.text = "$fileSize MB"

                download.setOnClickListener {
                    savefile(statusList[position])
                    dialog.dismiss()
                }
                dialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return statusList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image = itemView.findViewById<ImageView>(R.id.ivStatus)
        val download = itemView.findViewById<ImageView>(R.id.download)
        val play = itemView.findViewById<ImageView>(R.id.play)
        val card = itemView.findViewById<RelativeLayout>(R.id.card)
    }


    fun savefile(sourceuri: String) {
        val inpstrm: InputStream? = context.contentResolver.openInputStream(
            Uri.fromFile(File(sourceuri))
        )
        val fileName = Uri.parse(sourceuri).path
        var f: File? = null
        val extention = sourceuri.substring(sourceuri.lastIndexOf("."))
        f = File(
            Environment.getExternalStorageDirectory().absolutePath +
                    "/Download/ig_download/" +
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

    fun showPreview(imageUrl: String) {
        val inflater = LayoutInflater.from(context)
        val dialogView: View = inflater.inflate(R.layout.preview, null)

        val save = dialogView.findViewById<MaterialCardView>(R.id.save)
        val send = dialogView.findViewById<MaterialCardView>(R.id.send)
        val delete = dialogView.findViewById<MaterialCardView>(R.id.delete)
        val img = dialogView.findViewById<ImageView>(R.id.img)
        val close = dialogView.findViewById<ImageView>(R.id.close)
        Glide.with(context).load(imageUrl).into(img)


        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)

        val customDialog = dialogBuilder.create()

        close.setOnClickListener {
            customDialog.dismiss()
        }
        animateDialogIn(customDialog, context, R.anim.zoom_anim)
        customDialog.show()
    }

    private fun animateDialogIn(
        dialog: AlertDialog,
        context: Context,
        @AnimRes animationResId: Int
    ) {
        val animation = AnimationUtils.loadAnimation(context, animationResId)
        dialog.window?.decorView?.post {
            val revealAnimator = ViewAnimationUtils.createCircularReveal(
                dialog.window?.decorView,
                (dialog.window?.decorView?.width ?: 0) / 2,
                (dialog.window?.decorView?.height ?: 0) / 2,
                0f,
                dialog.window?.decorView?.width?.toFloat() ?: 0f
            )
            revealAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    dialog.show()
                }
            })
            revealAnimator.start()
        }
    }


}