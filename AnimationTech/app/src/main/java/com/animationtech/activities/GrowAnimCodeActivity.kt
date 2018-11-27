package com.animationtech.activities

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import com.animationtech.R
import kotlinx.android.synthetic.main.activity_grow_anim.*

/**
 * Created by Costi on 11/27/2018.
 */
class GrowAnimCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grow_anim)

        switchGrowOrShrink()
    }

    private fun switchGrowOrShrink(){
        swToGrow.setOnCheckedChangeListener { swToGrow, isChecked ->
            if(isChecked)
                growImage()
            else
                shrinkImage()
        }
    }

    private fun growImage(){
        faceIcon.animate()
                .scaleX(2f)
                .scaleY(2f)
                .rotationX(180f)
                .setInterpolator(AnticipateOvershootInterpolator())
                .duration = 2000
    }

    private fun shrinkImage(){
        faceIcon.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotationX(0f)
                .setInterpolator(BounceInterpolator())
                .duration = 2000

    }
}