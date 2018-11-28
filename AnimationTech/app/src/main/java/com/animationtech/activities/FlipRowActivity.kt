package com.animationtech.activities

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.animationtech.R
import kotlinx.android.synthetic.main.activity_flip_row.*
import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext

class FlipRowActivity : AppCompatActivity(), CoroutineScope {

    var sound: MediaPlayer? = null

    var job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flip_row)

        sound = MediaPlayer.create(this, R.raw.turn_page)

        btnTurn.setOnClickListener {

                flipRows()

        }

        btnClear.setOnClickListener { clear() }
    }

    fun flipRows(){
        launch {
            tvOne.rotate(360f, "I am turned upside")
            tvTwo.rotate(360f, "Now is my turn")
            tvThree.rotate(360f, "The last is the best")
        }

    }

    fun clear(){
        launch {
            tvOne.rotate(0f, "Back to origin one")
            tvTwo.rotate(0f, "I lost all points")
            tvThree.rotate(0f, "Now I am a winner")
        }
    }

    suspend fun TextView.rotate(rotation: Float, string: String){
        sound?.start()
        this.animate()
                .rotationX(rotation)
                //.setInterpolator(AnticipateOvershootInterpolator())
                .setInterpolator(BounceInterpolator())
                .setDuration(1200)



        delay(300)
        this.text = string

    }
}
