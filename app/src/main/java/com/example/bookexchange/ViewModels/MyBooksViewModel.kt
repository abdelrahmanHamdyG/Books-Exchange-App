package com.example.bookexchange.ViewModels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MyBooksViewModel: ViewModel() {


    lateinit var firebaseDatabase: DatabaseReference
    var myBooksList=MutableLiveData<ArrayList<Book>>()
    var _myImages=ArrayList<Bitmap>()
    var myImages=MutableLiveData<ArrayList<Bitmap>>()
    private var _myBooksList=ArrayList<Book>()



    suspend fun readTexts(uid:String){

        _myBooksList.clear()
        AppUtils.LOG("the readTexts current thread is ${Thread.currentThread().name}")
        firebaseDatabase = FirebaseDatabase.getInstance().reference

        try {


            var snapshot = firebaseDatabase.child("All Users").child(uid).child("Books").get().await()
            for (book in snapshot!!.children) {
                Log.i("my_trag", book.toString())
                val bookName = book.child("bookName").getValue(String::class.java)
                val bookDescription =
                    book.child("bookDescription").getValue(String::class.java)
                val category = book.child("category").getValue(String::class.java)
                val imageUri = book.child("imageUri").getValue(String::class.java)
                val user = book.child("user").getValue(String::class.java)
                val key = book.child("key").getValue(String::class.java)
                val city = book.child("city").getValue(String::class.java)

                _myBooksList.add(Book(bookName!!, bookDescription!!, category!!, imageUri!!, user!!, key!!, city!!))

            }

        }catch (e:Exception){



        }

        myBooksList.postValue(_myBooksList)
    }




}