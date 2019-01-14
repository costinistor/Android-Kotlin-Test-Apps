package simplestairs.com.bitmaptopdf

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File

class FilePath {

    fun getPath(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // LocalStorageProvider
            if (isLocalStorageDocument(uri)) {
                // The path is the id
                println("is in local")
                return DocumentsContract.getDocumentId(uri)
            } else if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                println("is in external")
                if ("primary".equals(type, ignoreCase = true)) {
                    println("is in primary")
                    return File(Environment.getExternalStorageDirectory(), "/" + split[1]).toString()
                }else{
                    println("is in home")
                    return File(Environment.getExternalStorageDirectory(), "/" + split[1]).toString()
                }
            } else if (isDownloadsDocument(uri)){
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                println("is in download")
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                println("is in media")
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
            else if(isGoogleDriveUri(uri)){
                return  uri.getLastPathSegment()
            }
            // MediaProvider
            // DownloadsProvider
            // ExternalStorageProvider
        } else if ("content".equals(uri.getScheme(), ignoreCase = true)){
            // Return the remote address
            println("is in content")
            return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(context, uri, null, null)
        } else if ("storage".equals(uri.getScheme(), ignoreCase = true)){
            // Return the remote address
            println("is in storage")
            return if (isGoogleDriveUri(uri)) uri.getLastPathSegment() else getDataColumn(context, uri, null, null)
        }
        else if ("file".equals(uri.getScheme(), ignoreCase = true)){
            println("is in file")
            return uri.getPath()
        }// File
        // MediaStore (and general)

        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is [LocalStorageProvider].
     */
    val AUTHORITY = "com.delaroystudios.filechoser.documents"
    fun isLocalStorageDocument(uri:Uri):Boolean {
        return AUTHORITY.equals(uri.getAuthority())
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri:Uri):Boolean {
        return "com.android.externalstorage.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri:Uri):Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri:Uri):Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri:Uri):Boolean {
        return "com.google.android.apps.photos.content" == uri.getAuthority()
    }

    fun isGoogleDriveUri(uri:Uri):Boolean {
        return "com.google.android.apps.docs.storage" == uri.getAuthority()
    }

    fun getDataColumn(context:Context, uri:Uri?, selection:String?,
                      selectionArgs:Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor!!.moveToFirst()) {
                val column_index = cursor!!.getColumnIndexOrThrow(column)
                return cursor!!.getString(column_index)
            }
        } finally {
            if (cursor != null)
                cursor!!.close()
        }
        return null
    }
}