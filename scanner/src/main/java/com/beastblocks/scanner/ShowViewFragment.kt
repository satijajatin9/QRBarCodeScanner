package com.beastblocks.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShowViewFragment : BottomSheetDialogFragment {
    var Views: Any? = null
    var BarQRScanner: BarQRScanner

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
        var vv: View
        if (Views is Int) {
            vv = inflater.inflate(Views as Int, container, false)
        } else {
            vv = Views as View
        }
        return vv
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BarQRScanner.DataShowing=false
    }
}