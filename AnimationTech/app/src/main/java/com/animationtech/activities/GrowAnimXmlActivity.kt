package com.animationtech.activities

import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.animationtech.R
import kotlinx.android.synthetic.main.activity_grow_anim.*

class GrowAnimXmlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grow_anim)

        switchGrowOrShrink()
    }

    fun switchGrowOrShrink(){
        swToGrow.setOnCheckedChangeListener { swToGrow, isChecked ->
            if(isChecked)
                growImage()
            else
                shrinkImage()
        }
    }

    fun growImage(){
//        val anim = AnimationUtils.loadAnimation(this, R.anim.grow)
//        faceIcon.startAnimation(anim)

        ObjectAnimator.ofFloat(faceIcon, "scaleX", 2f).apply {
            duration = 2000
            start()
        }
    }

    fun shrinkImage(){
        val anim = AnimationUtils.loadAnimation(this, R.anim.shrink)
        faceIcon.startAnimation(anim)
    }
}
