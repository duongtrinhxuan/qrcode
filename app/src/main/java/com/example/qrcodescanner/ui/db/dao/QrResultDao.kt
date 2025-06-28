package com.example.qrcodescanner.ui.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.qrcodescanner.ui.db.entities.QrResult
@Dao
interface QrResultDao {

    @Query("SELECT * FROM QrResult ORDER BY time DESC")
    fun getAllScannedResults(): List<QrResult>

    @Query("SELECT * FROM QrResult WHERE favourite = 1")
    fun getAllFavouriteResults(): List<QrResult>

    @Query("DELETE FROM QrResult")
    fun deleteAllScannedResults()

    @Query("DELETE FROM QrResult WHERE favourite = 1")
    fun deleteAllFavouriteResult()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertQrResult(qrResult: QrResult): Long

    @Query("SELECT * FROM QrResult WHERE id = :id")
    fun getQrResult(id: Int): QrResult

    @Query("UPDATE QrResult SET favourite = 1 WHERE id = :id")
    fun addToFavourite(id: Int): Int

    @Query("UPDATE QrResult SET favourite = 0 WHERE id = :id")
    fun removeFromFavourite(id: Int): Int
}