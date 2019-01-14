package simplestairs.com.bitmaptopdf

import android.content.Intent
import java.nio.file.Files.isDirectory
import android.provider.MediaStore
import android.content.ContentResolver
import android.graphics.Bitmap
import android.provider.DocumentsContract
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Environment.getExternalStorageDirectory
import android.os.Build
import android.net.Proxy.getHost
import android.net.Proxy.getPort
import android.database.DatabaseUtils
import android.net.Uri
import android.os.Environment
import android.support.annotation.RequiresApi
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileFilter
import java.text.DecimalFormat


@RequiresApi(Build.VERSION_CODES.KITKAT)
class FileUtils {

         val TAG = "FileUtils"
        private val DEBUG = false // Set to true to enable logging

         val MIME_TYPE_AUDIO = "audio/*"
         val MIME_TYPE_TEXT = "text/*"
         val MIME_TYPE_IMAGE = "image/*"
         val MIME_TYPE_VIDEO = "video/*"
         val MIME_TYPE_APP = "application/*"

         val HIDDEN_PREFIX = "."

        fun getExtension(uri:String?):String? {
        if (uri == null) {
            return null
        }

        val dot = uri!!.lastIndexOf(".")
            return if (dot >= 0) {
                uri!!.substring(dot)
            } else {
                // No extension.
                ""
            }
        }

        /**
        * @return Whether the URI is a local one.
        */
         fun isLocal(url:String?):Boolean {
            return if (url != null && !url!!.startsWith("http://") && !url!!.startsWith("https://")) {
                true
            } else false
        }

         fun isMediaUri(uri: Uri):Boolean {
            return "media".equals(uri.getAuthority(), ignoreCase = true)
        }

        /**
        * Convert File into Uri.
        */
         fun getUri(file: File?):Uri? {
            return if (file != null) {
                Uri.fromFile(file)
            } else null
        }

        /**
        * Returns the path only (without file name).
        */
        fun getPathWithoutFilename(file:File?):File? {
            if (file != null) {
                if (file!!.isDirectory()) {
                    // no file to be split off. Return everything
                   return file
                } else {
                    val filename = file!!.getName()
                    val filepath = file!!.getAbsolutePath()

                    // Construct path without file name.
                    var pathwithoutname = filepath.substring(0,
                    filepath.length - filename.length)
                    if (pathwithoutname.endsWith("/")) {
                        pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                    }
                    return File(pathwithoutname)
                }
            }
            return null
        }

        /**
        * @return The MIME type for the given file.
        */
        fun getMimeType(file:File):String? {
            val extension = getExtension(file.getName())
            return if (extension!!.length > 0) MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension!!.substring(1)) else "application/octet-stream"
        }

        /**
        * @return The MIME type for the give Uri.
        */
        fun getMimeType(context: Context, uri:Uri):String? {
            val file = File(getPath(context, uri))
            return getMimeType(file)
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

        /**
        * Get the value of the data column for this Uri. This is useful for
        * MediaStore Uris, and other file-based ContentProviders.
        *
        * @param context The context.
        * @param uri The Uri to query.
        * @param selection (Optional) Filter used in the query.
        * @param selectionArgs (Optional) Selection arguments used in the query.
        * @return The value of the _data column, which is typically a file path.
        * @author paulburke
        */
        fun getDataColumn(context:Context, uri:Uri?, selection:String?,
                            selectionArgs:Array<String>?):String? {

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)
                if (cursor != null && cursor!!.moveToFirst()) {
                    if (DEBUG)
                        DatabaseUtils.dumpCursor(cursor)

                    val column_index = cursor!!.getColumnIndexOrThrow(column)
                    return cursor!!.getString(column_index)
                }
            } finally {
                if (cursor != null)
                cursor!!.close()
                }
            return null
        }


        fun getPath(context:Context, uri:Uri): String? {
            if (DEBUG)
                println(
                    "Authority: " + uri.getAuthority() +
                    ", Fragment: " + uri.getFragment() +
                    ", Port: " + uri.getPort() +
                    ", Query: " + uri.getQuery() +
                    ", Scheme: " + uri.getScheme() +
                    ", Host: " + uri.getHost() +
                    ", Segments: " + uri.getPathSegments().toString()
                )

            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // LocalStorageProvider
                if (isLocalStorageDocument(uri)) {
                    // The path is the id
                    return DocumentsContract.getDocumentId(uri)
                } else if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return File(Environment.getExternalStorageDirectory(), "/" + split[1]).toString()
                    }
                } else if (isDownloadsDocument(uri)){
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))

                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri:Uri? = null
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
                }// MediaProvider
                // DownloadsProvider
                // ExternalStorageProvider
            } else if ("content".equals(uri.getScheme(), ignoreCase = true)){
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.getScheme(), ignoreCase = true)){
                return uri.getPath()
            }// File
            // MediaStore (and general)

            return null
        }

        /**
        * Convert Uri into File, if possible.
        *
        * @return file A local file that the Uri was pointing to, or null if the
        * Uri is unsupported or pointed to a remote resource.
        * @see .getPath
        * @author paulburke
        */
        fun getFile(context:Context, uri:Uri?):File? {
            if (uri != null) {
                val path = getPath(context, uri)
                if (path != null && isLocal(path)) {
                    return File(path)
                }
            }
            return null
        }

        fun getReadableFileSize(size:Int):String {
            val BYTES_IN_KILOBYTES = 1024
            val dec = DecimalFormat("###.#")
            val KILOBYTES = " KB"
            val MEGABYTES = " MB"
            val GIGABYTES = " GB"
            var fileSize = 0f
            var suffix = KILOBYTES

            if (size > BYTES_IN_KILOBYTES) {
                fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES
                    if (fileSize > BYTES_IN_KILOBYTES) {
                        fileSize = fileSize / BYTES_IN_KILOBYTES
                        suffix = GIGABYTES
                    } else {
                        suffix = MEGABYTES
                    }
                }
            }
            return (dec.format(fileSize) + suffix).toString()
        }

        fun getThumbnail(context:Context, file:File):Bitmap? {
            return getThumbnail(context, getUri(file), getMimeType(file))
        }

        fun getThumbnail(context:Context, uri:Uri):Bitmap? {
            return getThumbnail(context, uri, getMimeType(context, uri))
        }

        fun getThumbnail(context:Context, uri:Uri?, mimeType:String?):Bitmap? {
            var bm:Bitmap? = null
            if (uri != null) {
                val resolver = context.getContentResolver()
                var cursor:Cursor? = null
                try {
                    cursor = resolver.query(uri!!, null, null, null, null)
                    if (cursor!!.moveToFirst()) {
                        val id = cursor!!.getInt(0)

                        if (mimeType!!.contains("video")) {
                            bm = MediaStore.Video.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Video.Thumbnails.MINI_KIND, null)
                        } else if (mimeType!!.contains(MIME_TYPE_IMAGE)) {
                            bm = MediaStore.Images.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Images.Thumbnails.MINI_KIND, null)
                        }
                    }
                } catch (e:Exception) {
                        if (DEBUG)
                            println("getThumbnail" + e)
                } finally {
                        if (cursor != null)
                        cursor!!.close()
                }
            }
            return bm
        }

        /**
        * File and folder comparator. TODO Expose sorting option method
        *
        * @author paulburke
        */
         var sComparator:Comparator<File> = object: Comparator<File> {
            override fun compare(f1:File, f2:File):Int {
                // Sort alphabetically by lower case, which is much cleaner
                        return f1.getName().toLowerCase().compareTo(
                f2.getName().toLowerCase())
            }
        }

        /**
        * File (not directories) filter.
        *
        * @author paulburke
        */
         var sFileFilter:FileFilter = object: FileFilter {
            override fun accept(file:File):Boolean {
                val fileName = file.getName()
                // Return files only (not directories) and skip hidden files
                        return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX)
            }
        }

        /**
        * Folder (directories) filter.
        *
        * @author paulburke
        */
         var sDirFilter: FileFilter = object: FileFilter {
            override fun accept(file:File):Boolean {
                val fileName = file.getName()
                // Return directories only and skip hidden directories
                        return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX)
            }
        }


         fun createGetContentIntent():Intent {
            // Implicitly allow the user to select a particular kind of data
                val intent = Intent(Intent.ACTION_GET_CONTENT)
            // The MIME data type filter
                intent.type = "*/*"
            // Only return URIs that can be opened with ContentResolver
                intent.addCategory(Intent.CATEGORY_OPENABLE)
            return intent
        }
}