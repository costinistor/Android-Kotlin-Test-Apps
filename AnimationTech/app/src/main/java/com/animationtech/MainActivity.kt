package com.animationtech

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.animationtech.activities.FlipRowActivity
import com.animationtech.activities.GrowAnimCodeActivity
import com.animationtech.activities.GrowAnimXmlActivity
import com.animationtech.animtwoactivities.AnimFirstActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAnimGrowXml.setOnClickListener { openGrowAnimXml() }
        btnAnimGrowCode.setOnClickListener { openGrowAnimCode() }
        btnToFlip.setOnClickListener { openFlipRow() }
        btnAnimActivity.setOnClickListener { openAnimTwoActivities() }
    }

    private fun openGrowAnimXml() = startActivity(Intent(this, GrowAnimXmlActivity::class.java))
    private fun openGrowAnimCode() = startActivity(Intent(this, GrowAnimCodeActivity::class.java))
    private fun openFlipRow() = startActivity(Intent(this, FlipRowActivity::class.java))
    private fun openAnimTwoActivities() = startActivity(Intent(this, AnimFirstActivity::class.java))
}
