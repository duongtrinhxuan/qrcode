package com.example.qrcodescanner.ui.db

import com.example.qrcodescanner.ui.db.database.QrResultDatabase
import com.example.qrcodescanner.ui.db.entities.QrResult
import java.util.Calendar

class DBHelper (var qrResultDatabase: QrResultDatabase) : DBHelperI {
    override fun insertQrResult(result: String, image: ByteArray?): Int {
      val time = Calendar.getInstance()
        val resultType = "TEXT"
        val qrResult = QrResult(
            result = result,
            resultType = resultType,
            calendar = time,
            favourite = false,
            image = image
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

    override fun getAllQrScannedResult(): List<QrResult> {
        return qrResultDatabase.getQrDao().getAllScannedResults()
    }

    override fun getAllFavouriteQrScannedResult(): List<QrResult> {
        return qrResultDatabase.getQrDao().getAllFavouriteResults()
    }

    override fun deleteQrResult(id: Int): Int {
        return qrResultDatabase.getQrDao().deleteQrResult(id)
    }

    override fun deleteAllScannedResults() {
        qrResultDatabase.getQrDao().deleteAllScannedResults()
    }

    override fun deleteAllFavouriteQrScannerResults() {
        qrResultDatabase.getQrDao().deleteAllFavouriteResult()
    }
}