package com.example.bookexchange

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.ByteArrayOutputStream

object AppUtils {
    const val TAG="my_trag"
     val categoryMap = mapOf(
        "Scientific" to 0,
        "Religious" to 1,
        "Novels" to 2,
         "Historic" to 3
    )
     val texts = arrayOf(
        "All",
        "Religious",
        "Biography",
        "Historic",
        "Dictionary",
        "Novels",
        "Scientific",
         "Favourites"

        )


    fun showToast(context: Context,string :String){

        Toast.makeText(context,string,Toast.LENGTH_LONG).show()
    }

    fun LOG(string:String){


        Log.i(TAG,string)

    }

    fun resizeBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
    }

    fun getResizedImageByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }



}