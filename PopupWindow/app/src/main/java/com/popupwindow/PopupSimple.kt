package com.popupwindow

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics

/**
 * Created by Costi on 11/20/2018.
 */
class PopupSimple: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.popup_window)

        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width * 0.8).toInt(), (height * 0.6).toInt())
    }
}