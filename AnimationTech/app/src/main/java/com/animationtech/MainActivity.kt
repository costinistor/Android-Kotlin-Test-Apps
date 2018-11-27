package com.animationtech

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.animationtech.activities.GrowAnimCodeActivity
import com.animationtech.activities.GrowAnimXmlActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAnimGrowXml.setOnClickListener { openGrowAnimXml() }
        btnAnimGrowCode.setOnClickListener { openGrowAnimCode() }
    }

    fun openGrowAnimXml() = startActivity(Intent(this, GrowAnimXmlActivity::class.java))
    fun openGrowAnimCode() = startActivity(Intent(this, GrowAnimCodeActivity::class.java))
}
