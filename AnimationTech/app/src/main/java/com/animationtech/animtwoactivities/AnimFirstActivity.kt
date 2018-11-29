package com.animationtech.animtwoactivities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.animationtech.R
import kotlinx.android.synthetic.main.activity_anim_first.*
import android.util.Pair


class AnimFirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim_first)

        btnToHappyFace.setOnClickListener { openHappyFace() }
    }

    fun openHappyFace(){
        val intent = Intent(this, AnimSecondActivity::class.java)

        val imgTran = Pair<View, String>(imageHappy, "imageTransition")
        val nameTran = Pair<View, String>(textName, "nameTransition")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

            val options = ActivityOptions.makeSceneTransitionAnimation(this, imgTran, nameTran)

            startActivity(intent, options.toBundle())

        }else{
            startActivity(intent)
        }



    }


}
