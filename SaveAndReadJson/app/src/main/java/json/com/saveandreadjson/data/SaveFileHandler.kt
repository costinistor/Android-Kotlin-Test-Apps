package json.com.saveandreadjson.data

import android.content.Context

fun savingDataToFile(context: Context, file: String, data: String){
    val fileOut = context.openFileOutput(file, Context.MODE_PRIVATE)
    fileOut.write(data.toByteArray())
    println("file is saved")
    fileOut.close()
}

fun loadDataFromFile(context: Context, file: String, defaultData: String): String{
    try{
        val fileInput = context.openFileInput(file)
        val text = fileInput.readBytes()
        fileInput.close()
        return String(text)
    }catch(e: Exception){
        return defaultData
    }
}