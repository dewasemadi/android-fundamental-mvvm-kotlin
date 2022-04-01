package com.dicoding.githubuser.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R

fun ImageView.setImage(context: Context, resourceId: String?) {
    Glide.with(context).load(resourceId).circleCrop()
        .placeholder(R.drawable.blank_image).circleCrop()
        .into(this)
}