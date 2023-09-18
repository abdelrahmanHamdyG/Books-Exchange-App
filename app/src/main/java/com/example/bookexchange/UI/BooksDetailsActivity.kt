package com.example.bookexchange.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bookexchange.R

class BooksDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books_details)


             var bookKey=intent.getStringExtra("book_key")



    }
}