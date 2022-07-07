package com.beastblocks.scanner

import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap


interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)

    fun qrCodeNotFound()
}