package com.beastblocks.barqrcodescanner

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.beastblocks.scanner.BarQRScanner
import com.beastblocks.scanner.CreateFragmentListener
import com.beastblocks.scanner.QRCodeFoundListener
import com.beastblocks.scanner.ShowViewFragment

class MainActivity : AppCompatActivity(), QRCodeFoundListener, CreateFragmentListener {
    lateinit var barQRScanner: BarQRScanner
    lateinit var showViewFragment: ShowViewFragment
    lateinit var demo: TextView
    var QRCodeData: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        barQRScanner = findViewById(R.id.barcodescanner)
        barQRScanner.init(this, this, true, true)
        barQRScanner.enableHapticFeedback(true)
    }

    override fun onQRCodeFound(qrCode: String?) {
        showViewFragment =
            barQRScanner.showDialogFragment(
                R.layout.demo,
                supportFragmentManager,
                barQRScanner,
                this
            )!!
        QRCodeData = qrCode
    }

    override fun qrCodeNotFound() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (barQRScanner.GALLERY == requestCode && resultCode == RESULT_OK && data != null) {
            val QRCodeData: String? = barQRScanner.ScanImage(data!!)
            if (!QRCodeData.isNullOrEmpty()) {
                showViewFragment = barQRScanner.showDialogFragment(
                    R.layout.demo,
                    supportFragmentManager,
                    barQRScanner, this
                )!!

            }
        }
    }

    fun INit(view: View) {
        demo = view.findViewById(R.id.demo)
    }

    override fun onViewCreated(view: View) {
        INit(view)
        demo.setText(QRCodeData)
    }
}