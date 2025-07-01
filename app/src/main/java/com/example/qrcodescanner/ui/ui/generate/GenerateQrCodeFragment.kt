package com.example.qrcodescanner.ui.ui.generate

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.qrcodescanner.R
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream

class GenerateQrCodeFragment : Fragment() {

    private lateinit var qrImageView: ImageView
    private lateinit var inputEditText: EditText
    private lateinit var colorPickerButton: Button
    private lateinit var generateButton: Button
    private lateinit var downloadButton: Button
    private lateinit var shareButton: Button

    private var qrBitmap: Bitmap? = null
    private var qrColor: Int = Color.BLACK

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_generate_qr_code, container, false)
        qrImageView = view.findViewById(R.id.qrImageView)
        inputEditText = view.findViewById(R.id.inputEditText)
        colorPickerButton = view.findViewById(R.id.colorPickerButton)
        generateButton = view.findViewById(R.id.generateButton)
        downloadButton = view.findViewById(R.id.downloadButton)
        shareButton = view.findViewById(R.id.shareButton)

        colorPickerButton.setOnClickListener { showColorPicker() }
        generateButton.setOnClickListener { generateQrCode() }
        downloadButton.setOnClickListener { downloadQrCode() }
        shareButton.setOnClickListener { shareQrCode() }

        return view
    }

    private fun showColorPicker() {
        val colors = arrayOf("Đen", "Đỏ", "Xanh lá", "Xanh dương", "Tím", "Vàng")
        val colorValues = arrayOf(Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.YELLOW)
        AlertDialog.Builder(requireContext())
            .setTitle("Chọn màu QR")
            .setItems(colors) { _, which ->
                qrColor = colorValues[which]
                colorPickerButton.background = ColorDrawable(qrColor)
            }
            .show()
    }

    private fun generateQrCode() {
        val text = inputEditText.text.toString()
        if (text.isBlank()) {
            Toast.makeText(requireContext(), "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val barcodeEncoder = BarcodeEncoder()
            qrBitmap = barcodeEncoder.encodeBitmap(
                text,
                BarcodeFormat.QR_CODE,
                800, 800,
                mapOf(com.google.zxing.EncodeHintType.MARGIN to 1)
            ).copy(Bitmap.Config.ARGB_8888, true)

            // Đổi màu QR code
            qrBitmap = qrBitmap?.let { recolorQrBitmap(it, qrColor) }

            qrImageView.setImageBitmap(qrBitmap)
            downloadButton.visibility = View.VISIBLE
            shareButton.visibility = View.VISIBLE
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Lỗi tạo QR: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recolorQrBitmap(bitmap: Bitmap, color: Int): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        for (x in 0 until w) {
            for (y in 0 until h) {
                val pixel = bitmap.getPixel(x, y)
                if (pixel == Color.BLACK) {
                    out.setPixel(x, y, color)
                } else {
                    out.setPixel(x, y, Color.WHITE)
                }
            }
        }
        return out
    }

    private fun downloadQrCode() {
        qrBitmap?.let { bitmap ->
            val filename = "qr_${System.currentTimeMillis()}.png"
            val savedUri = MediaStore.Images.Media.insertImage(
                requireContext().contentResolver, bitmap, filename, "QR Code"
            )
            if (savedUri != null) {
                Toast.makeText(requireContext(), "Đã lưu vào thư viện!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Lưu thất bại!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareQrCode() {
        qrBitmap?.let { bitmap ->
            try {
                val file = File(requireContext().cacheDir, "qr_share.png")
                val fOut = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
                fOut.flush()
                fOut.close()
                val uri: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().packageName + ".provider",
                    file
                )
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/png"
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ QR Code"))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Chia sẻ thất bại: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}