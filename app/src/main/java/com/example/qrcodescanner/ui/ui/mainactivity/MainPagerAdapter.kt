package com.example.qrcodescanner.ui.ui.mainactivity

import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.qrcodescanner.ui.ui.Scanner.QrScannerFragment
import com.example.qrcodescanner.ui.ui.scanned_history.ScannedHistoryFragment

class MainPagerAdapter(var fm: androidx.fragment.app.FragmentManager) : FragmentStatePagerAdapter(fm)
{

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            0 -> QrScannerFragment.newInstance()
            1 -> ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.ALL_RESULT)
            2 -> ScannedHistoryFragment.newInstance(ScannedHistoryFragment.ResultListType.FAVORITE_RESULT)
            else -> QrScannerFragment.newInstance()
        }
    }
}