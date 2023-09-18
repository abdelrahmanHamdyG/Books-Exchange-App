package com.example.bookexchange

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

object AppUtils {
    const val TAG="my_trag"

    fun showToast(context: Context,string :String){

        Toast.makeText(context,string,Toast.LENGTH_LONG).show()
    }

    fun LOG(string:String){


        Log.i(TAG,string)

    }

}