package com.example.qrcodescanner.ui.ui.dialogs

import android.app.Dialog
import android.app.DialogFragment
import android.app.KeyguardManager.KeyguardDismissCallback
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.qrcodescanner.ui.db.entities.QrResult
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.db.DBHelper
import com.example.qrcodescanner.ui.db.DBHelperI
import com.example.qrcodescanner.ui.db.database.QrResultDatabase
import com.example.qrcodescanner.ui.ui.utils.toFormattedDisplay
import android.graphics.BitmapFactory
import android.net.Uri
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import android.provider.MediaStore
import androidx.core.content.FileProvider
class QrCodeResultDialog (
    var context : Context ,
    private val onDismissCallback: (() -> Unit)? = null )
{
    private lateinit var dialog : Dialog

    private var qrResult : QrResult? = null
    private lateinit var favouriteIcon : androidx.appcompat.widget.AppCompatImageView
    private lateinit var shareResult: androidx.appcompat.widget.AppCompatImageView
    private lateinit var copyResult: androidx.appcompat.widget.AppCompatImageView
    private lateinit var cancelDialog: androidx.appcompat.widget.AppCompatImageView
    private lateinit var scannedDate: androidx.appcompat.widget.AppCompatTextView
    private lateinit var scannedText: androidx.appcompat.widget.AppCompatTextView
    private lateinit var qrImageView: androidx.appcompat.widget.AppCompatImageView
    private lateinit var dbHelperI: DBHelperI
    init {
        init()
        initDialog()
    }

    private fun init() {
        dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(context.applicationContext))
    }
    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(true)
        dialog.setOnDismissListener{
            onDismissCallback?.invoke()
        }
        favouriteIcon = dialog.findViewById(R.id.favouriteIcon)
        shareResult = dialog.findViewById(R.id.shareResult)
        copyResult = dialog.findViewById(R.id.copyResult)
        cancelDialog = dialog.findViewById(R.id.cancelDialog)
        scannedText = dialog.findViewById(R.id.scannedText)
        scannedDate = dialog.findViewById(R.id.scannedDate)
        qrImageView = dialog.findViewById(R.id.qrImageView)
        onClicks()
    }

    fun show(qrResult: QrResult){
        this.qrResult = qrResult
        scannedDate.text = qrResult.calendar.toFormattedDisplay()
        scannedText.text = qrResult.result
        favouriteIcon.isSelected = qrResult.favourite
        qrResult.image?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            qrImageView.setImageBitmap(bitmap)
        } ?: qrImageView.setImageResource(android.R.color.transparent)
        dialog.show()
    }

    private fun onClicks() {
        favouriteIcon.setOnClickListener {
            if(favouriteIcon.isSelected){
                removeFromFavourites()
            } else {
                addToFavourites()
            }
        }
        shareResult.setOnClickListener {
            shareQrImage()
        }
        copyResult.setOnClickListener {
            copyResultToClipBoard()
        }
        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun addToFavourites() {
        favouriteIcon.isSelected = true
        qrResult?.id?.let { id ->
            Thread {
                dbHelperI.addToFavourite(id)
            }.start()
        }
    }

    private fun removeFromFavourites() {
        favouriteIcon.isSelected = false
        qrResult?.id?.let { id ->
            Thread {
                dbHelperI.removeFromFavourite(id)
            }.start()
        }
    }

    private fun shareQrImage() {
        qrResult?.image?.let { imageBytes ->
            try {
                val file = File(context.cacheDir, "qr_share.png")
                val fos = FileOutputStream(file)
                fos.write(imageBytes)
                fos.flush()
                fos.close()
                val uri: Uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".provider",
                    file
                )
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/png"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ QR Code"))
            } catch (e: Exception) {
                Toast.makeText(context, "Chia sẻ thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(context, "Không có hình ảnh QR để chia sẻ", Toast.LENGTH_SHORT).show()
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("QRScannerResult", scannedText.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}