package simplestairs.com.bitmaptopdf

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_pdf_one.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.KITKAT)
class PdfOneActivity : AppCompatActivity() {

    var bitmap: Bitmap? = null
    var pdfDocument: PdfDocument? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_one)

        btnPickImage.setOnClickListener { pickImage() }
    }

    fun pickImage(){
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 120)
        }
        else {
            EasyPermissions.requestPermissions(this, "Access for storage",
                101, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private val galleryPermissions =
        arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 120 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            setImage(selectedImageUri)
            createPdf()
            savePdf()
        }
    }

    fun setImage(imageUri: Uri){
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(imageUri, filePath, null, null, null)
        cursor.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePath[0])
        val myPath = cursor.getString(columnIndex)

        bitmap = BitmapFactory.decodeFile(myPath)
        cursor.close()
        myImage.setImageBitmap(bitmap)
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

    fun savePdf(){
        val root = File(Environment.getExternalStorageDirectory(), "PDF Folder")
        if(!root.exists())
            root.mkdir()

        val file = File(root, "picture.pdf")

        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument?.writeTo(fileOutputStream)
        }catch (e:Exception){

        }

        pdfDocument?.close()
    }



    //        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            != PackageManager.PERMISSION_GRANTED) {
//            // Permission is not granted
//            println("Nu ai permisioune booooo")
//        }
}
