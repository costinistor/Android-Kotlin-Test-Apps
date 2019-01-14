package simplestairs.com.bitmaptopdf

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_pdf_two.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.URI

@RequiresApi(Build.VERSION_CODES.KITKAT)
class PdfTwoActivity : AppCompatActivity() {

    var bitmap: Bitmap? = null
    var pdfDocument: PdfDocument? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_two)

        btnPrint.setOnClickListener {
            printScreen(layoutFirst)
            //savePdf("print.pdf")
        }

        btnOpen.setOnClickListener {
            //openFolder()
            sendToDrive()
        }
    }

    fun sendToDrive(){
        var intent = Intent(Intent.ACTION_SEND)
        //intent.addCategory(Intent.CATEGORY_OPENABLE)
        //intent.putExtra(Intent.EXTRA_STREAM, "")
        //intent.putExtra(Intent.EXTRA_EMAIL, Array<String>(10){""})
        //intent.putExtra(Intent.EXTRA_SUBJECT, "Hopa")
        //intent.putExtra(Intent.EXTRA_TEXT, getMessage() + "\n \n \n" + getThanks())
        //intent.type = "message/rfc822"
        var uri = Uri.parse(Environment.DIRECTORY_DOWNLOADS)
        intent.setDataAndType(uri, "application/png")
        //intent.type = "application/pdf"
        startActivity(Intent.createChooser(intent, "Open drive"))
    }

    // action to write to folder
//    fun openFolder(){
//        var intent = Intent.createChooser(openIntent(), "Save to pdf")
//        startActivityForResult(intent, 101)
//    }
//
//    fun openIntent(): Intent
//    {
//        var intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
//        intent.type = "application/pdf"
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.putExtra(Intent.EXTRA_TITLE, "my.pdf")
//        return intent
//    }

    // action to read from folders
    fun openFolder(){
        var intent = Intent.createChooser(openIntent(), "Open Me")
        startActivityForResult(intent, 101)
    }

    fun openIntent(): Intent
    {
        var intent = Intent(Intent.ACTION_GET_CONTENT)
        var uri = Uri.parse(Environment.DIRECTORY_DOWNLOADS)
        //intent.type = "application/pdf"
        intent.setDataAndType(uri, "application/png")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 101 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedUri = data.data
            println("file : $selectedUri")
            var fl = FilePath()
            var path = fl.getPath(this, selectedUri)

//            printScreen(layoutFirst)
//            savePdf(path!!)
            Toast.makeText(this, "file select is: $path", Toast.LENGTH_LONG).show()
        }
    }

    private fun printScreen(view: View){
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, (bitmap.width * 1.4).toInt(), 3).create()
//        pdfDocument?.pages?.add(pageInfo)
//        pdfDocument?.pages?.add(pageInfo)
        val page = pdfDocument?.startPage(pageInfo)

        val canvas = page?.canvas
        //val canvas = Canvas(bitmap)
        canvas?.drawColor(Color.parseColor("#E8F5F9"))
        view.draw(canvas)
//        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap!!.width, bitmap!!.height, true)
//        canvas?.drawBitmap(bitmap, 0f, 0f, null)


        pdfDocument?.finishPage(page)

    }

    fun createPdf(){
        pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap!!.width, bitmap!!.height, 1).create()
        val page = pdfDocument?.startPage(pageInfo)
        val canvas = page?.canvas
        val paint = Paint()
        paint.color = Color.parseColor("#ffffff")
        canvas?.drawPaint(paint)

        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap!!.width, bitmap!!.height, true)
        paint.color = Color.BLUE
        canvas?.drawBitmap(bitmap, 0f, 0f, null)

        pdfDocument?.finishPage(page)
    }

    fun savePdf(uri: String){
//        val root = File(Environment.getExternalStorageDirectory(), "PDF Folder")
//        if(!root.exists())
//            root.mkdir()
//
//        val file = File(root, name)

        val file = File(uri)
        try {

            val fileOutputStream = FileOutputStream(file)
            pdfDocument?.writeTo(fileOutputStream)
            println("file saved: $file")
        }catch (e:Exception){

        }

        pdfDocument?.close()
    }
}
