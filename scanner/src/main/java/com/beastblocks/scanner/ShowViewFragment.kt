package com.beastblocks.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ShowViewFragment : BottomSheetDialogFragment {
    private var Views: Any? = null
    private var BarQRScanner: BarQRScanner
    private var Listener: CreateFragmentListener
    var finalView: View?=null

    constructor(view: Int, barQRScanner: BarQRScanner,listener: CreateFragmentListener) {
        Views = view
        BarQRScanner = barQRScanner
        Listener = listener
    }

    constructor(view: View, barQRScanner: BarQRScanner,listener: CreateFragmentListener) {
        Views = view
        BarQRScanner = barQRScanner
        Listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (Views is Int) {
            finalView = inflater.inflate(Views as Int, container, false)
        } else {
            finalView = Views as View
        }
        Listener.onViewCreated(finalView!!)
        return finalView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        BarQRScanner.DataShowing = false
    }
}