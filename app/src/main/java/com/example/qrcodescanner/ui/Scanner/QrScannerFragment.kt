package com.example.qrcodescanner.ui.Scanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qrcodescanner.R

class QrScannerFragment : Fragment() {
     companion object {
         fun newInstance(): QrScannerFragment {
             return QrScannerFragment()
         }
     }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_scanner, container, false)
    }

}