package com.example.bookexchange.UI

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.example.bookexchange.R

class MakeRequestActivity : AppCompatActivity() {
    private val IMAGE_PICK_REQUEST_CODE=6
    lateinit var myImage:ImageView
    lateinit var hisImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_request)

        myImage=findViewById(R.id.make_request_my_image)


        myImage.setOnClickListener {

        }





    }
}