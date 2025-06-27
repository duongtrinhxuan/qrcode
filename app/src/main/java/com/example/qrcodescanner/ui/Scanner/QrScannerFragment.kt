package com.example.qrcodescanner.ui.Scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.qrcodescanner.R
import com.journeyapps.barcodescanner.ZxingScannerView

class QrScannerFragment : Fragment() {
     companion object {
         fun newInstance(): QrScannerFragment {
             return QrScannerFragment()
         }
     }

    private lateinit var mView: View
    private lateinit var scannerView : ZXingScannerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_qr_scanner, container, false)
        initializeQrScanner()
        return mView.rootView
    }

    private fun initializeQrScanner() {
        scannerView = DecoratedBarcodeView(context)
        scannerView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorTranslucent))
        scannerView.setBorderColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        scannerView.setLaserColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark))
        scannerView.setBorderStrokeWidth(10)
        scannerView.setautofocus(true)
        scannerView.setSquareViewFinder(true)
    }

}