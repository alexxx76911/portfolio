package com.example.aperture.util

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

fun <T : Fragment> T.withArguments(action: Bundle.() -> Unit): T {
    return apply {
        arguments = Bundle().apply(action)
    }
}


fun Int.toPx() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics).toInt()


fun RecyclerView.ViewHolder.loadImage(url: String, view: ImageView, onFail: () -> Boolean, onSuccess: (resource: Drawable?) -> Boolean) {
    Glide.with(itemView)
            .load(url)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                ): Boolean {
                    return onFail()
                }

                override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                ): Boolean {
                    return onSuccess(resource)
                }


            })
            .into(view)
}

fun RecyclerView.ViewHolder.loadImage(file: File, view: ImageView, onFail: () -> Boolean, onSuccess: (resource: Drawable?) -> Boolean) {
    Glide.with(itemView)
            .load(file)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                ): Boolean {
                    return onFail()
                }

                override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                ): Boolean {
                    return onSuccess(resource)
                }


            })
            .into(view)
}

fun RecyclerView.ViewHolder.loadImage(url: String, view: ImageView) {
    Glide.with(itemView)
            .load(url)
            .into(view)
}

fun RecyclerView.ViewHolder.loadImage(bytes: ByteArray, view: ImageView) {
    Glide.with(itemView)
            .load(bytes)
            .into(view)
}