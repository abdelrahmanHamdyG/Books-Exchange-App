package com.example.bookexchange.UI

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R


class MakeRequestActivity : AppCompatActivity(),FinishingFragmentListener {
    lateinit var myBooks:RecyclerView
    lateinit var hisImageView: ImageView
    lateinit var dialog: ChoosingBooksDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_request)

        myBooks=findViewById(R.id.make_request_my_recycler)


        myBooks.setOnClickListener {

            dialog = ChoosingBooksDialog()
            dialog.setListener(this)
            dialog.show(supportFragmentManager, "CustomDialog")

        }


    }

    override fun setListener(arr: ArrayList<Book>) {

        dialog.dismiss()
        for(i in arr)
            AppUtils.showToast(this,i.bookName!!)

    }
}