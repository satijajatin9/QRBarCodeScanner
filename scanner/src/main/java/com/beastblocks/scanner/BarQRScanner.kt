package com.beastblocks.scanner

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.AttributeSet
import android.util.Size
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import java.lang.Exception
import java.util.concurrent.ExecutionException


class BarQRScanner : RelativeLayout, View.OnClickListener {

    val GALLERY = 1011

    private var showViewFragment: ShowViewFragment? = null
    private var listener: QRCodeFoundListener? = null
    private var Flash: ImageView? = null
    private var Gallery: ImageView? = null
    private var CameraView: PreviewView? = null
    private var TopLine: View? = null
    private var activity: Activity? = null

    private var BottomLine: View? = null

    private var TopLay: RelativeLayout? = null
    private var BottomLay: RelativeLayout? = null

    private var RightLay: RelativeLayout? = null
    private var LeftLay: RelativeLayout? = null

    private var LeftLine: View? = null
    private var RightLine: View? = null
    private var CenterScanLay: LinearLayout? = null

    private var RightBlack: View? = null
    private var TopBlack: View? = null
    private var BottomBlack: View? = null
    private var LeftBlack: View? = null

    private var CenterBlankSpace: View? = null
    private var ParentLay: LinearLayout? = null
    private var FunctionsLay: LinearLayout? = null

    private var ScannerHeight = 700
    private var ExtraWidth = 10
    private var hapticFeedback = false
    private var BorderColor = R.color.white
    private var galleryEnabled = false
    private var flashEnabled = false
    internal var DataShowing = false
    private var mFlashMode = false
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    fun init(
        act: Activity,
        FlashEnabled: Boolean = false,
        GalleryEnabled: Boolean = false,
        borderColor: Int = R.color.white
    ) {
        activity = act
        flashEnabled = FlashEnabled
        galleryEnabled = GalleryEnabled
        BorderColor = borderColor
        flashEnabled = FlashEnabled
        designView(context)
    }


    private fun designView(context: Context) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            mCameraId = mCameraManager!!.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        CameraView = PreviewView(context)
        TopLine = View(context)

        BottomLine = View(context)

        TopLay = RelativeLayout(context)
        BottomLay = RelativeLayout(context)

        RightLay = RelativeLayout(context)
        LeftLay = RelativeLayout(context)

        LeftLine = View(context)
        RightLine = View(context)
        CenterScanLay = LinearLayout(context)

        RightBlack = View(context)
        TopBlack = View(context)
        BottomBlack = View(context)
        LeftBlack = View(context)

        CenterBlankSpace = View(context)
        ParentLay = LinearLayout(context)
        FunctionsLay = LinearLayout(context)
        Flash = ImageView(context)
        Gallery = ImageView(context)

        Gallery?.setImageResource(com.beastblocks.scanner.R.drawable.gallery)
        Flash?.setImageResource(com.beastblocks.scanner.R.drawable.flashoff)
        Gallery?.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
        Flash?.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
        Gallery?.setOnClickListener(this)
        Flash?.setOnClickListener(this)

        ParentLay?.orientation = LinearLayout.VERTICAL
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FunctionsLay?.orientation = LinearLayout.VERTICAL
        } else {
            FunctionsLay?.orientation = LinearLayout.HORIZONTAL
        }
        TopBlack?.setBackgroundColor(resources.getColor(R.color.black))
        TopBlack?.alpha = 0.5f
        BottomBlack?.setBackgroundColor(resources.getColor(R.color.black))
        BottomBlack?.alpha = 0.5f
        RightBlack?.setBackgroundColor(resources.getColor(R.color.black))
        RightBlack?.alpha = 0.5f
        LeftBlack?.setBackgroundColor(resources.getColor(R.color.black))
        LeftBlack?.alpha = 0.5f

        TopLine?.setBackgroundColor(resources.getColor(BorderColor))
        BottomLine?.setBackgroundColor(resources.getColor(BorderColor))
        RightLine?.setBackgroundColor(resources.getColor(BorderColor))
        LeftLine?.setBackgroundColor(resources.getColor(BorderColor))
        requestRunTimePermissionForAccessCamera()
        if (flashEnabled) {
            FunctionsLay?.addView(Flash)
        }
        if (galleryEnabled) {
            FunctionsLay?.addView(Gallery)
        }

        TopLay?.addView(TopBlack)
        TopLay?.addView(TopLine)
        ParentLay?.addView(TopLay)


        LeftLay?.addView(LeftBlack)
        LeftLay?.addView(LeftLine)
        CenterScanLay?.addView(LeftLay)

        CenterScanLay?.addView(CenterBlankSpace)


        RightLay?.addView(RightBlack)
        RightLay?.addView(RightLine)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RightLay?.addView(FunctionsLay)
        }
        CenterScanLay?.addView(RightLay)

        ParentLay?.addView(CenterScanLay)



        BottomLay?.addView(BottomBlack)
        BottomLay?.addView(BottomLine)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            BottomLay?.addView(FunctionsLay)
        }
        ParentLay?.addView(BottomLay)

        addView(CameraView)
        addView(ParentLay)
        setLayoutParams()
    }


    private fun setLayoutParams() {
        Flash?.layoutParams = LinearLayout.LayoutParams(
            80,
            80
        ).also {
            it.setMargins(50, 50, 50, 50)
        }
        Gallery?.layoutParams = LinearLayout.LayoutParams(
            80,
            80
        ).also {
            it.setMargins(50, 50, 50, 50)
        }
        FunctionsLay?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).also {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                it.addRule(RelativeLayout.CENTER_VERTICAL)
            } else {
                it.addRule(RelativeLayout.CENTER_HORIZONTAL)
            }

        }
        CameraView?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        ParentLay?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        TopBlack?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        BottomBlack?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        RightBlack?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        LeftBlack?.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )

        TopLine?.layoutParams =
            RelativeLayout.LayoutParams(ScannerHeight + (ExtraWidth * 2), ExtraWidth).apply {
                addRule(RelativeLayout.CENTER_HORIZONTAL)
                addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
            }
        BottomLine?.layoutParams =
            RelativeLayout.LayoutParams(ScannerHeight + (ExtraWidth * 2), ExtraWidth).apply {
                addRule(RelativeLayout.CENTER_HORIZONTAL)
            }
        LeftLine?.layoutParams =
            RelativeLayout.LayoutParams(ExtraWidth, RelativeLayout.LayoutParams.MATCH_PARENT)
                .apply {
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                }
        RightLine?.layoutParams =
            RelativeLayout.LayoutParams(ExtraWidth, RelativeLayout.LayoutParams.MATCH_PARENT)
                .apply {
                    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                }
        TopLay?.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f)
        BottomLay?.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f)
        RightLay?.layoutParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
        LeftLay?.layoutParams =
            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f)
        CenterBlankSpace?.layoutParams = LinearLayout.LayoutParams(ScannerHeight, ScannerHeight)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View) {
        when (view) {
            Flash -> {
                if (mFlashMode) {
                    mFlashMode = false
                    Flash?.setImageResource(com.beastblocks.scanner.R.drawable.flashoff)
                    Flash?.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
                    switchFlashLight(false)
                } else {
                    mFlashMode = true
                    Flash?.setImageResource(com.beastblocks.scanner.R.drawable.flashon)
                    Flash?.imageTintList = null
                    switchFlashLight(true)
                }
            }
            Gallery -> {
                requestRunTimePermissionForAccessStorage()
            }
        }
    }


    // request runtime permissions for camera
    private fun requestRunTimePermissionForAccessCamera() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        ) {
            Toast.makeText(context, "Grant Camera Permissions to use Scanner", Toast.LENGTH_LONG)
                .show()
        } else {
            startCamera()
        }
    }

    private fun requestRunTimePermissionForAccessStorage() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(context, "Grant Storage Permissions to use Scanner", Toast.LENGTH_LONG)
                .show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    val i = Intent()
                    i.type = "image/*"
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    i.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(
                        activity!!,
                        Intent.createChooser(i, "Select Picture"),
                        GALLERY,
                        null
                    )
                } else {
                    val intent = Intent()
                    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                    val uri = Uri.fromParts("package", context.getPackageName(), null)
                    intent.data = uri
                    context.startActivity(intent)
                }
            } else {
                val i = Intent()
                i.type = "image/*"
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                i.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    activity!!,
                    Intent.createChooser(i, "Select Picture"),
                    GALLERY,
                    null
                )
            }
        }
    }


    private fun startCamera() {
        cameraProviderFuture!!.addListener({
            try {
                cameraProvider = cameraProviderFuture!!.get()
                startCameraAnalyzer()
            } catch (e: ExecutionException) {
                Toast.makeText(
                    context,
                    "Error starting camera " + e.localizedMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
            } catch (e: InterruptedException) {
                Toast.makeText(
                    context,
                    "Error starting camera " + e.localizedMessage,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun startCameraAnalyzer() {
        CameraView?.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW)

        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(CameraView?.createSurfaceProvider())

        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            QRCodeImageAnalyzer(object : QRCodeFoundListener {
                override fun onQRCodeFound(qrCode: String?) {
                    if (hapticFeedback && !DataShowing) {
                        vibrate()
                    }
                    listener?.onQRCodeFound(qrCode)
                }

                override fun qrCodeNotFound() {
                    listener?.qrCodeNotFound()
                }

            })
        )

        camera = cameraProvider!!.bindToLifecycle(
            context as LifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )
    }

    private fun switchFlashLight(status: Boolean) {
        try {
            camera?.cameraControl?.enableTorch(status)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun vibrate() {
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            v?.vibrate(100)
        }
    }

    fun ScanImage(bMap: Bitmap): String? {
        var contents: String? = null;
        try {
            var intArray = IntArray(bMap.getWidth() * bMap.getHeight())
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

            var source: LuminanceSource =
                RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            var bitmap = BinaryBitmap(HybridBinarizer(source))

            var reader: Reader = MultiFormatReader()
            var result: Result = reader.decode(bitmap)
            contents = result.getText()
            if (hapticFeedback && !DataShowing) {
                vibrate()
            }
        }catch (e:Exception){
        }
        return contents
    }


    fun ScanImage(data: Intent): String? {
        var contents: String? = null;
        try {
            val imageurl: Uri? = data.getData()
            val filePath = imageurl
            val bMap: Bitmap = getBitmapFromUri(filePath!!)
            var intArray = IntArray(bMap.getWidth() * bMap.getHeight())
            bMap.getPixels(
                intArray,
                0,
                bMap.getWidth(),
                0,
                0,
                bMap.getWidth(),
                bMap.getHeight()
            )
            var source: LuminanceSource =
                RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            var bitmap = BinaryBitmap(HybridBinarizer(source))
            var reader: Reader = MultiFormatReader()
            var result: Result = reader.decode(bitmap)
            contents = result.getText()
            if (hapticFeedback && !DataShowing) {
                vibrate()
            }
        } catch (e: Exception) {
        }
        return contents
    }

    fun ScanImage(imageurl: Uri): String? {
        var contents: String? = null;
        try {
            val filePath = imageurl
            val bMap: Bitmap = getBitmapFromUri(filePath!!)
            var intArray = IntArray(bMap.getWidth() * bMap.getHeight())
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
            var source: LuminanceSource =
                RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            var bitmap = BinaryBitmap(HybridBinarizer(source))
            var reader: Reader = MultiFormatReader()
            var result: Result = reader.decode(bitmap)
            contents = result.getText()
            if (hapticFeedback && !DataShowing) {
                vibrate()
            }
        } catch (e: Exception) {
        }
        return contents
    }

    override fun setOnFocusChangeListener(l: OnFocusChangeListener?) {
        super.setOnFocusChangeListener(l)
        mFlashMode = false
        Flash?.setImageResource(com.beastblocks.scanner.R.drawable.flashoff)
        Flash?.imageTintList = ColorStateList.valueOf(resources.getColor(R.color.white))
        switchFlashLight(false)
    }

    fun showDialogFragment(
        view: View,
        fragmentManager: FragmentManager,
        barQRScanner: BarQRScanner
    ): ShowViewFragment? {
        if (!DataShowing) {
            DataShowing = true
            showViewFragment = ShowViewFragment(view, barQRScanner)
            showViewFragment!!.show(fragmentManager, "ShowView")
        }
        return showViewFragment!!
    }

    fun showDialogFragment(
        view: Int,
        fragmentManager: FragmentManager,
        barQRScanner: BarQRScanner
    ): ShowViewFragment? {
        if (!DataShowing) {
            DataShowing = true
            showViewFragment = ShowViewFragment(view, barQRScanner)
            showViewFragment!!.show(fragmentManager, "ShowView")
        }
        return showViewFragment!!
    }

    fun setQRCodeScannerListener(qrCodeFoundListener: QRCodeFoundListener) {
        listener = qrCodeFoundListener
    }

    fun getViewFragment(): ShowViewFragment? {
        return showViewFragment
    }

    fun setScanBoxHeightWidth(value: Int) {
        ScannerHeight = value
        setLayoutParams()
    }

    fun setScanBoxOutlineWidth(value: Int) {
        ExtraWidth = value
        setLayoutParams()
    }

    fun enableHapticFeedback(value: Boolean) {
        hapticFeedback = value
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? =
            context.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }
}