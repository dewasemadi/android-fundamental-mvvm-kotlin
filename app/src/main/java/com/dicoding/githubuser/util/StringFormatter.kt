package com.dicoding.githubuser.util

import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun Long.prettyCount(): String {
    val array = arrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
    val value = floor(log10(this.toDouble())).toInt()
    val base = value / 3
    return if (value >= 3 && base < array.size) {
        DecimalFormat("#0.0").format(this / 10.0.pow((base * 3).toDouble())) + array[base]
    } else {
        DecimalFormat("#,##0").format(this)
    }
}
