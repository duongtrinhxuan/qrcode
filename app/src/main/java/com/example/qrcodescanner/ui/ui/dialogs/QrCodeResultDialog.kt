package com.example.qrcodescanner.ui.ui.dialogs

import android.app.Dialog
import android.app.DialogFragment
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.qrcodescanner.ui.db.entities.QrResult
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.ui.utils.toFormattedDisplay

class QrCodeResultDialog (var context : Context) {
    private lateinit var dialog : Dialog

    private var qrResult : QrResult? = null
    private lateinit var favouriteIcon : androidx.appcompat.widget.AppCompatImageView
    private lateinit var shareResult: androidx.appcompat.widget.AppCompatImageView
    private lateinit var copyResult: androidx.appcompat.widget.AppCompatImageView
    private lateinit var cancelDialog: androidx.appcompat.widget.AppCompatImageView
    private lateinit var scannedDate: androidx.appcompat.widget.AppCompatTextView
    private lateinit var scannedText: androidx.appcompat.widget.AppCompatTextView

    init {
        initDialog()
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(true)
        favouriteIcon = dialog.findViewById(R.id.favouriteIcon)
        shareResult = dialog.findViewById(R.id.shareResult)
        copyResult = dialog.findViewById(R.id.copyResult)
        cancelDialog = dialog.findViewById(R.id.cancelDialog)
        scannedText = dialog.findViewById(R.id.scannedText)
        scannedDate = dialog.findViewById(R.id.scannedDate)
        onClicks()
    }

    fun show(qrResult: QrResult){
        this.qrResult = qrResult
        scannedDate.text = qrResult.calendar.toFormattedDisplay()
        scannedText.text = qrResult.result
        favouriteIcon.isSelected = qrResult.favourite
        dialog.show()
    }

    private fun onClicks() {
        favouriteIcon.setOnClickListener {

        }
        shareResult.setOnClickListener {
            sharedResult()
        }
        copyResult.setOnClickListener {
            copyResultToClipBoard()
        }
        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }

    }

    private fun sharedResult() {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(Intent.EXTRA_TEXT, scannedText.text.toString())
        context.startActivity(txtIntent)
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = ClipData.newPlainText("QRScannerResult", scannedText.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}