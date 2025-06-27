package com.example.qrcodescanner.ui.mainactivity

import androidx.fragment.app.FragmentStatePagerAdapter

class MainPagerAdapter(var fm: androidx.fragment.app.FragmentManager) : FragmentStatePagerAdapter(fm)
{

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            0 -> com.example.qrcodescanner.ui.Scanner.QrScannerFragment.newInstance()
            1 -> com.example.qrcodescanner.ui.scanned_history.ScannedHistoryFragment.newInstance()
            2 -> com.example.qrcodescanner.ui.scanned_history.ScannedHistoryFragment.newInstance()
            else -> com.example.qrcodescanner.ui.Scanner.QrScannerFragment.newInstance()
        }
    }
}