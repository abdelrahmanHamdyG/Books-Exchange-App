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
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MyBooksViewModel: ViewModel() {


    lateinit var firebaseDatabase: DatabaseReference
    var myBooksList=MutableLiveData<ArrayList<Book>>()
    var _myImages=ArrayList<Bitmap>()
    var myImages=MutableLiveData<ArrayList<Bitmap>>()
    private var _myBooksList=ArrayList<Book>()



    suspend fun readTexts(uid:String){

        _myBooksList.clear()
        firebaseDatabase = FirebaseDatabase.getInstance().reference

        try {


            val snapshot = firebaseDatabase.child("All Users").child(uid).child("Books").get().await()
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
                val state=book.child("state").getValue(String::class.java)

                _myBooksList.add(Book(bookName!!, bookDescription!!, category!!, imageUri!!, user!!, key!!, city!!,state!!))
            }

        }catch (e:Exception){



        }

        myBooksList.postValue(_myBooksList)
    }
    suspend fun removeValue(key:String,uid:String,imageName:String):Boolean{

        val firebaseDataBase = FirebaseDatabase.getInstance().getReference()
        var theReturn=true;

        val job1= GlobalScope.launch(Dispatchers.IO) {

            try {
                firebaseDataBase.child("AllBooks").child(key).removeValue().await()
            }catch (e:Exception){

                theReturn=false;
            }

        }

        val job2= GlobalScope.launch(Dispatchers.IO) {
            try {


                val response =
                    firebaseDataBase.child("All Users").child(uid).child("Books").get().await()
                for (i in response.children) {
                    val book = i.getValue(Book::class.java)
                    if (book!!.key == key) {
                        firebaseDataBase.child("All Users").child(uid).child("Books").child(i.key!!)
                            .removeValue().await()
                    }

                }
            }catch (e:Exception){
                theReturn=false;

            }
        }
        val job3=GlobalScope.launch (Dispatchers.IO){

            val firebaseStorage=FirebaseStorage.getInstance().reference

            try {
                firebaseStorage.child(imageName).delete().await()
            }catch (e:Exception){
                theReturn=false;
            }

        }
        job1.join()
        job2.join()
        job3.join()
        return theReturn;

    }





}