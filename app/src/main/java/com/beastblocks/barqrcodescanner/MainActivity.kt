package com.beastblocks.barqrcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.beastblocks.scanner.BarQRScanner
import com.beastblocks.scanner.QRCodeFoundListener
import com.beastblocks.scanner.ShowViewFragment

class MainActivity : AppCompatActivity(), QRCodeFoundListener {
    lateinit var barQRScanner: BarQRScanner
    lateinit var showViewFragment: ShowViewFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barQRScanner = findViewById(R.id.barcodescanner)
        barQRScanner.init(this,this,true, true)
        barQRScanner.enableHapticFeedback(true)
    }

    override fun onQRCodeFound(qrCode: String?) {
        var view = TextView(this)
        view.text = qrCode
        showViewFragment =
            barQRScanner.showDialogFragment(view, supportFragmentManager, barQRScanner)!!
    }

    override fun qrCodeNotFound() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (barQRScanner.GALLERY == requestCode && resultCode == RESULT_OK && data != null) {
            val QRCodeData: String? = barQRScanner.ScanImage(data!!)
            if (!QRCodeData.isNullOrEmpty()) {
                showViewFragment = barQRScanner.showDialogFragment(QRCodeData, supportFragmentManager, barQRScanner)!!
            }
        }
    }
}