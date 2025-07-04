package com.example.qrcodescanner.ui.db

import com.example.qrcodescanner.ui.db.entities.QrResult

interface DBHelperI {
    fun insertQrResult(result : String, image: ByteArray?): Int

    fun getQrResult(id: Int): QrResult

    fun addToFavourite(id: Int): Int

    fun removeFromFavourite(id: Int): Int

    fun getAllQrScannedResult(): List<QrResult>

    fun getAllFavouriteQrScannedResult(): List<QrResult>

    fun deleteQrResult(id: Int): Int

    fun deleteAllScannedResults()

    fun deleteAllFavouriteQrScannerResults()
}