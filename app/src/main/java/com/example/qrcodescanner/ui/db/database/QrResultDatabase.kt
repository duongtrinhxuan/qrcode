package com.example.qrcodescanner.ui.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.qrcodescanner.ui.db.dao.QrResultDao
import com.example.qrcodescanner.ui.db.entities.QrResult

@Database(entities = [QrResult::class], version = 1)
abstract class QrResultDatabase : RoomDatabase() {
    abstract fun getQrDao(): QrResultDao

    companion object {
        private const val DB_NAME = "QrResultDatabase"
        @Volatile
        private var INSTANCE: QrResultDatabase? = null

        fun getAppDatabase(context: Context): QrResultDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QrResultDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}