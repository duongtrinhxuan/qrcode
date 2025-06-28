package com.example.qrcodescanner.ui.ui.mainactivity

import java.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.viewpager.widget.ViewPager
import com.example.qrcodescanner.R
import com.example.qrcodescanner.ui.db.database.QrResultDatabase
import com.example.qrcodescanner.ui.db.entities.QrResult
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setViewPagerAdapter()
        setBottomNavigation()
        setViewPagerListener()

        val qrResult = QrResult(
            result = "Dummy Text",
            resultType = "Text",
            favourite = false,
            calendar = Calendar.getInstance()
        )
        Thread {
            QrResultDatabase.getAppDatabase(this).getQrDao().insertQrResult(qrResult)
            Log.d("Inserted QR Result: ${qrResult.result}","them thanh cong")
        }.start()
    }



    private fun setBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            viewPager.currentItem =when (it.itemId) {
                R.id.scanMenuId -> 0
                R.id.recentScannedMenuId -> 1
                R.id.favouritesMenuId -> 2
                else -> 0
            }
            true
        }
    }

    private fun setViewPagerAdapter() {
        viewPager.adapter = MainPagerAdapter(supportFragmentManager)
    }
    private fun setViewPagerListener() {
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId =   when(position) {
                    0 -> R.id.scanMenuId
                    1 -> R.id.recentScannedMenuId
                    2 -> R.id.favouritesMenuId
                    else -> R.id.scanMenuId
                }
            }
        })
    }
}


