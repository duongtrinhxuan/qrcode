package com.example.qrcodescanner.ui.ui.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun Calendar.toFormattedDisplay (): String {
   val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US)
    return simpleDateFormat.format(this.time)
}