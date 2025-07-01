package com.example.qrcodescanner.ui.ui.mainactivity

import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.qrcodescanner.ui.ui.Scanner.QrScannerFragment
import com.example.qrcodescanner.ui.ui.scanned_history.ScannedHistoryFragment
import com.example.qrcodescanner.ui.ui.generate.GenerateQrCodeFragment
class MainPagerAdapter(var fm: androidx.fragment.app.FragmentManager) : FragmentStatePagerAdapter(fm)
{

    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            0 -> QrScannerFragment.newInstance()
            1 -> ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.ALL_RESULT)
            2 -> ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.FAVORITE_RESULT)
            3  -> GenerateQrCodeFragment()
            else -> QrScannerFragment.newInstance()
        }
    }
}