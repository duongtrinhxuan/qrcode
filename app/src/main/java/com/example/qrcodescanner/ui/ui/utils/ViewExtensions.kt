package com.example.qrcodescanner.ui.ui.utils

import android.annotation.SuppressLint
import android.view.View
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Calendar.toFormattedDisplay (): String {
   val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
    return simpleDateFormat.format(this.time)
}