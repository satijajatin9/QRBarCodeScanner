package com.beastblocks.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShowViewFragment : BottomSheetDialogFragment {
    private var Views: Any? = null
    private var BarQRScanner: BarQRScanner

    constructor(view: Int, barQRScanner: BarQRScanner) {
        Views = view
        BarQRScanner = barQRScanner
    }

    constructor(view: View, barQRScanner: BarQRScanner) {
        Views = view
        BarQRScanner = barQRScanner
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var finalView: View
        if (Views is Int) {
            finalView = inflater.inflate(Views as Int, container, false)
        } else {
            finalView = Views as View
        }
        return finalView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BarQRScanner.DataShowing = false
    }
}