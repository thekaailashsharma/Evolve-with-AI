package com.test.palmapi.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

/**Save Bitmap To Gallery
 * @param bitmap The bitmap to be saved in Storage/Gallery*/
fun saveBitmapImage(bitmap: Bitmap, context: Context) {
    val timestamp = System.currentTimeMillis()

    //Tell the media scanner about the new file so that it is immediately available to the user.
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    values.put(MediaStore.Images.Media.DATE_ADDED, timestamp)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        values.put(MediaStore.Images.Media.DATE_TAKEN, timestamp)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + "PalmApi")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            try {
                val outputStream =  context.contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.close()
                    } catch (e: Exception) {
                        Log.e("TAG", "saveBitmapImage: ", e)
                    }
                }
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)

                Toast.makeText( context, "Saved...", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("TAG", "saveBitmapImage: ", e)
            }
        }
    } else {
        val imageFileFolder = File(Environment.getExternalStorageDirectory().toString() + '/' + "PalmApi")
        if (!imageFileFolder.exists()) {
            imageFileFolder.mkdirs()
        }
        val mImageName = "$timestamp.png"
        val imageFile = File(imageFileFolder, mImageName)
        try {
            val outputStream: OutputStream = FileOutputStream(imageFile)
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                Log.e("TAG", "saveBitmapImage: ", e)
            }
            values.put(MediaStore.Images.Media.DATA, imageFile.absolutePath)
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

            Toast.makeText( context, "Saved...", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("TAG", "saveBitmapImage: ", e)
        }
    }
}