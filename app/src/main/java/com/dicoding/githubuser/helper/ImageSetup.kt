package com.dicoding.githubuser.helper

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(context: Context, resourceId: Int?) {
    Glide.with(context).load(resourceId).circleCrop().into(this)
}