package com.example.qrcodescanner.ui.db.converters

import androidx.room.TypeConverters
import java.util.Calendar

class DateTimeConverters {
    @TypeConverters
    fun toCalendar(l: Long): Calendar? {
       val c = Calendar.getInstance()
       c!!.timeInMillis = l
        return c
    }
    @TypeConverters
    fun fromCalendar(c: Calendar?): Long? {
        return c?.time?.time
    }
}