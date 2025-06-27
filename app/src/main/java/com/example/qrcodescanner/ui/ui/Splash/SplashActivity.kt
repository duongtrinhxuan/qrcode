package com.example.qrcodescanner.ui.ui.Splash

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.qrcodescanner.ui.ui.mainactivity.MainActivity
import com.example.qrcodescanner.R
import java.util.logging.Handler

class SplashActivity : AppCompatActivity() {
    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            checkForPermission()
        }, 3000)
    }

    private fun checkForPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            gotoMainActivity()
        } else
            requestThePermission()

    }

    private fun requestThePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun isUserPermanentlyDenied(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA).not()
        } else {
            return false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                gotoMainActivity()
            } else if (isUserPermanentlyDenied()) {
               showGoToAppSettingsDialog()
            }
            else
                requestThePermission()
        }
    }

    private fun showGoToAppSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Grant Camera Permission")
            .setMessage("We need camera permission to scan QR codes. Please go to settings and enable it.")
            .setPositiveButton("Grant") { dialog, _ ->
                gotoAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(this, "We need camera permission for Start this Application", Toast.LENGTH_SHORT).show()
                finish()
            }
            .show()
    }
    private fun gotoAppSettings() {
       var intent = android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = android.net.Uri.fromParts("package", packageName, null)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun gotoMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onRestart() {
        super.onRestart()
        checkForPermission()
    }
}
