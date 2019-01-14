package simplestairs.com.bitmaptopdf

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Layout
import android.text.style.BackgroundColorSpan
import android.util.Size
import android.view.View
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.fonts.otf.TableHeader
import kotlinx.android.synthetic.main.activity_pdf_three.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception


class PdfThreeActivity : AppCompatActivity() {
    val STORAGE_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_three)

        btnIPrint.setOnClickListener { printToPdf() }
    }

    fun printToPdf(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                val permission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, STORAGE_CODE)
            }else{
                // permission is granted call savePdf()
                savePdf()
            }
        }else{
            // system OS < marshmallow
            savePdf()
        }
    }

    fun savePdf(){
        writePdf()
//        val doc = Document()
//        val fileName = etName.text.toString()
//        val filePath = Environment.getExternalStorageDirectory().toString() + "/" + fileName + ".pdf"
//        try {
//            PdfWriter.getInstance(doc, FileOutputStream(filePath))
//            doc.open()
//
//            doc.addAuthor("Moi Costica")
//            doc.add(Paragraph(tvTitle.text.toString()))
//            doc.close()
//            Toast.makeText(this, "$fileName.pdf is saved in $filePath", Toast.LENGTH_SHORT).show()
//
//        }catch (e:Exception){
//            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//        }
    }

    fun writePdf(){
        var stream = ByteArrayOutputStream()
        printScreen(layoutIFirst).compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var stream2 = ByteArrayOutputStream()
        printScreen(layoutISecond).compress(Bitmap.CompressFormat.JPEG, 100, stream2)

//        val rect = Rectangle(PageSize.A4)
//        rect.backgroundColor = BaseColor.BLUE

        val doc = Document(PageSize.A4)
        val fileName = etName.text.toString()
        val root = File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS)
        if(!root.exists())
            root.mkdir()

        val file = File(root, "$fileName.pdf")

        try {
            var pdfWriter = PdfWriter.getInstance(doc, FileOutputStream(file))

            doc.open()

            //doc.pageSize = Rectangle(layoutIFirst.width.toFloat() * 1.2f, layoutIFirst.width.toFloat() * 1.8f)
            doc.addAuthor("Moi Costica")

            val widthImage = PageSize.A4.width - doc.leftMargin() - doc.rightMargin()
            println("Distanta exacta este $widthImage")
            val w = PageSize.A4
            val image = Image.getInstance(stream.toByteArray())
            val image2 = Image.getInstance(stream2.toByteArray())
            image.scaleToFit(widthImage - 323f, PageSize.A4.height)
            image2.scaleToFit(widthImage - 203f, PageSize.A4.height)

            var p = Paragraph()
            p.font = Font(Font.FontFamily.HELVETICA, 26f)
            p.alignment = Paragraph.ALIGN_CENTER
            var c = Chunk("Stairs-X calculator")


            p.add(c)

            doc.add(p)
            doc.add(Paragraph(tvTitle.text.toString()))
//            var a = Paragraph()
//
//            var b = Chunk(image, 0f, -100f)
//            a.add(b)
//
//            b = Chunk(image2, 0f, -102f)
//            a.add(b)
//            doc.add(a)
//            //doc.add(image)
//            //doc.add(image2)
//
//            doc.newPage()
            var col = ColumnText(pdfWriter.directContent)
            val col1 = Rectangle(36f, 76f, 236f, 776f)
            col1.backgroundColor = BaseColor.GREEN
            col1.borderWidth = 5f
            col1.borderColor = BaseColor.RED
            var columns = arrayOf(col1, Rectangle(239f, 60f, 559f, 806f))

            col.addElement(image)
            col.addElement(image)
            col.addElement(image)
            col.setSimpleColumn(columns[0])

            col.go()

            col.addElement(image2)
            col.setSimpleColumn(columns[1])
            col.go()

            doc.newPage()
            var table  = PdfPTable(2)
            table.totalWidth = 530f

            var cel1 = PdfPCell()

            cel1.backgroundColor = BaseColor.GREEN
            cel1.fixedHeight = 700f
            cel1.addElement(image)
            table.addCell(cel1)

            var cel2 = PdfPCell()
            cel2.backgroundColor = BaseColor.GRAY
            cel2.fixedHeight = 700f
            cel2.addElement(image2)
            table.addCell(cel2)

            doc.add(table)

            doc.close()
            Toast.makeText(this, "$fileName.pdf is saved in $file", Toast.LENGTH_LONG).show()

        }catch (e:Exception){

        }

    }

    private fun printScreen(view: View): Bitmap {
        var bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.parseColor("#E8F5F9"))
        view.draw(canvas)
        return bitmap
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            STORAGE_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permission granted, call savePdf()
                    savePdf()
                }else{
                    // permission denied, shoe error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


//    fun scaleImage(){
//        //if you would have a chapter indentation
//        val indentation = 0
//        //whatever
//        val image = coolPic
//
//        val scaler = (document.getPageSize().getWidth() - document.leftMargin()
//                - document.rightMargin() - indentation) / image.getWidth() * 100
//
//        image.scalePercent(scaler)
//    }
}
