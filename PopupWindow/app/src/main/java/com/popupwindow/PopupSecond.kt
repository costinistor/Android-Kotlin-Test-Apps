package com.popupwindow

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics

class PopupSecond : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_second)

        var dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        var width = dm.widthPixels
        var height = dm.heightPixels

        window.setLayout((width * 0.8).toInt(), (height * 0.6).toInt())
    }
}
