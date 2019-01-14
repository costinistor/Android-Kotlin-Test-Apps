package simplestairs.com.bitmaptopdf

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPdfOne.setOnClickListener { openPdfOneActivity() }
        btnPdfTwo.setOnClickListener { openPdfTwoActivity() }
        btnPdfThree.setOnClickListener { openPdfThreeActivity() }
    }

    private fun openPdfOneActivity(){
        startActivity(Intent(this, PdfOneActivity::class.java))
    }

    private fun openPdfTwoActivity(){
        startActivity(Intent(this, PdfTwoActivity::class.java))
    }

    private fun openPdfThreeActivity(){
        startActivity(Intent(this, PdfThreeActivity::class.java))
    }
}
