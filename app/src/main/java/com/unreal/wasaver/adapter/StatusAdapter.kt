package com.unreal.wasaver.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
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
import com.unreal.allvideodownloader.utils.Util
import com.unreal.wasaver.R
import com.unreal.wasaver.fragment.CustomFullScreenDialog
import java.io.*


class StatusAdapter(
    private val statusList: ArrayList<String>,
    val context: Context,
    val activity: Activity,
) : RecyclerView.Adapter<StatusAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.whatsapp_status_column, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val actualPosition = holder.adapterPosition
        Glide.with(context).load(statusList[actualPosition]).into(holder.image)
        holder.download.setOnClickListener {
            Util.savefile(statusList[actualPosition], context)
        }
        holder.card.setOnLongClickListener(object : OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                showPreview(statusList[actualPosition])
                return true
            }

        })
        if (statusList[actualPosition].endsWith(".mp4")) {
            holder.play.visibility = View.VISIBLE
            holder.image.setOnClickListener {
                val dialog = Dialog(holder.image.context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.setContentView(R.layout.video_dialog)
                val download = dialog.findViewById<Button>(R.id.dwVideo)
                val player = dialog.findViewById<VideoView>(R.id.videoPlayer)
                player.setVideoURI(Uri.parse(statusList[actualPosition]))
                val mediaController = MediaController(holder.image.context)
                mediaController.setAnchorView(player)
                mediaController.setMediaPlayer(player)
                player.setMediaController(mediaController)
                player.start()
                val fileName = dialog.findViewById<TextView>(R.id.fileName)
                val Size = dialog.findViewById<TextView>(R.id.fileSize)
                val extention =
                    statusList[actualPosition].substring(statusList[actualPosition].lastIndexOf("."))
                val fileDescriptor = context.contentResolver
                    .openAssetFileDescriptor(Uri.fromFile(File(statusList[actualPosition])), "r")
                val fileSize = fileDescriptor!!.length / 1024 / 1024
                fileName.text = "${System.currentTimeMillis()}$extention"
                Size.text = "$fileSize MB"

                download.setOnClickListener {
                    Util.savefile(statusList[actualPosition], context)
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


    fun showPreview(imageUrl: String) {
        val inflater = LayoutInflater.from(context)
        val fullScreenDialog = CustomFullScreenDialog(context, context as Activity, imageUrl)
        fullScreenDialog.show()

        /*val dialogView: View = inflater.inflate(R.layout.preview, null)

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
        }*/
        animateDialogIn(fullScreenDialog, context, R.anim.zoom_anim)
        //customDialog.show()
    }

    private fun animateDialogIn(
        dialog: Dialog,
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