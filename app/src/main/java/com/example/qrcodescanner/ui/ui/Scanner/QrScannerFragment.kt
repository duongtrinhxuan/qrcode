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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        barcodeView = mView.findViewById(R.id.barcode_scanner)
        flashToggle = mView.findViewById(R.id.flashToggle)
        initializeQrScanner()
        setOnClick()
        return mView.rootView
    }



    private fun initializeQrScanner() {
        // Cấu hình barcode formats nếu muốn (ví dụ chỉ quét QR_CODE)
        val formats = listOf(com.google.zxing.BarcodeFormat.QR_CODE)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.decodeContinuous(object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult?) {
                result?.text?.let {
                        handleResult(it)
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
        Toast.makeText(requireContext(), "QR Content: $result", Toast.LENGTH_SHORT).show()
        barcodeView.resume()
    }
}