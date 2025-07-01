package com.example.qrcodescanner.ui.ui.Scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.db.DBHelper
import com.example.qrcodescanner.ui.db.DBHelperI
import com.example.qrcodescanner.ui.db.database.QrResultDatabase
import com.example.qrcodescanner.ui.ui.dialogs.QrCodeResultDialog
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
class QrScannerFragment : Fragment() {
    companion object {
        fun newInstance(): QrScannerFragment {
            return QrScannerFragment()
        }
    }

    private lateinit var mView: View
    private lateinit var barcodeView: DecoratedBarcodeView
    private lateinit var flashToggle: ImageView
    private var lastResult: String? = null

    private lateinit var resultDialog : QrCodeResultDialog

    private lateinit var dbHelperI: DBHelperI
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        barcodeView = mView.findViewById(R.id.barcode_scanner)
        flashToggle = mView.findViewById(R.id.flashToggle)
        init()
        initViews()
        setOnClick()
        return mView.rootView
    }

    private fun init() {
        dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(requireContext().applicationContext))
    }

    private fun initViews() {
        initializeQrScanner()
        setResultDialog()
    }

    private fun setResultDialog() {
        resultDialog = QrCodeResultDialog(requireContext())
        {
            lastResult = null
        }
    }


    private fun initializeQrScanner() {
        // Cấu hình barcode formats nếu muốn (ví dụ chỉ quét QR_CODE)
        val formats = listOf(com.google.zxing.BarcodeFormat.QR_CODE)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.text?.let {
                    if (it != lastResult) {
                        lastResult = it
                        handleResult(it)
                    }
                }
            }
            override fun possibleResultPoints(resultPoints: List<com.google.zxing.ResultPoint>) {}
        })
        startQrCamera()
    }

    private fun startQrCamera() {
        barcodeView.resume()
    }

    override fun onResume() {
        super.onResume()
        lastResult = null
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        barcodeView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        barcodeView.pause()
    }
    private fun setOnClick() {
        flashToggle.setOnClickListener {
            if (!flashToggle.isSelected) {
                onFlashLight()
            } else {
                offFlashLight()
            }
        }
    }

    private fun onFlashLight() {
        flashToggle.isSelected = true
        barcodeView.setTorchOn()
    }

    private fun offFlashLight() {
        flashToggle.isSelected = false
        barcodeView.setTorchOff()
    }
    private fun handleResult(result: String) {
        onQrResult(result)
    }

    private fun onQrResult(text: String) {
        if (text.isNullOrEmpty()){
            Toast.makeText(context, "Empty Qr Code", Toast.LENGTH_SHORT).show()
        } else {
            saveToDatabase(text)
        }
    }

    private fun saveToDatabase(result: String) {
        Thread {
            // Tạo bitmap QR code từ text
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(
                result,
                BarcodeFormat.QR_CODE,
                800, 800
            )
            // Chuyển bitmap thành byte array
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageBytes = stream.toByteArray()

            val insertedRowid = dbHelperI.insertQrResult(result, imageBytes)
            val qrResult = dbHelperI.getQrResult(insertedRowid)
            requireActivity().runOnUiThread {
                val dialog = QrCodeResultDialog(requireContext()) {
                    lastResult = null
                }
                dialog.show(qrResult)
            }
        }.start()
    }
}