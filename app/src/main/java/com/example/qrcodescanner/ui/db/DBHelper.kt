package com.example.qrcodescanner.ui.db

import com.example.qrcodescanner.ui.db.database.QrResultDatabase
import com.example.qrcodescanner.ui.db.entities.QrResult
import java.util.Calendar

class DBHelper (var qrResultDatabase: QrResultDatabase) : DBHelperI {
    override fun insertQrResult(result: String): Int {
      val time = Calendar.getInstance()
        val resultType = "TEXT"
        val qrResult = QrResult(
            result = result,
            resultType = resultType,
            calendar = time,
            favourite = false
        )
        return qrResultDatabase.getQrDao().insertQrResult(qrResult).toInt()
    }

    override fun getQrResult(id: Int): QrResult {
        return qrResultDatabase.getQrDao().getQrResult(id)
    }

    override fun addToFavourite(id: Int): Int {
        return qrResultDatabase.getQrDao().addToFavourite(id)
    }

    override fun removeFromFavourite(id: Int): Int {
        return qrResultDatabase.getQrDao().removeFromFavourite(id)
    }
}