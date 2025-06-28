package com.example.qrcodescanner.ui.db

import com.example.qrcodescanner.ui.db.entities.QrResult

interface DBHelperI {
    fun insertQrResult(result : String): Int

    fun getQrResult(id: Int): QrResult
}