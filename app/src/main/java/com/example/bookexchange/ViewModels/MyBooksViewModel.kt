package com.example.bookexchange.ViewModels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MyBooksViewModel: ViewModel() {


    lateinit var firebaseDatabase: DatabaseReference
    var myBooksList=MutableLiveData<ArrayList<Book>>()

    private var _myBooksList=ArrayList<Book>()



    suspend fun readTexts(uid:String){

        _myBooksList.clear()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit= Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()


        val api =retrofit.create(ApiInterface::class.java)

        try{

            val books=api.getMyBooks(uid).await()

            _myBooksList.addAll(books)

        }catch (e:Exception){

            Log.e("API_CALL", "API call failed: ${e.message}")
        }


        /*
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

                _myBooksList.add(Book(bookName!!, bookDescription!!, category!!, imageUri!!, user!!,state!!))
            }

        }catch (e:Exception){



        }


         */


        myBooksList.postValue(_myBooksList)
    }
    suspend fun removeValue(key:String,uid:String,imageName:String):Boolean{

        val firebaseDataBase = FirebaseDatabase.getInstance().getReference()
        var theReturn=true;

        val job1=viewModelScope.launch(Dispatchers.IO) {

            try {
                firebaseDataBase.child("AllBooks").child(key).removeValue().await()
            }catch (e:Exception){

                theReturn=false;
            }

        }

        val job2= viewModelScope.launch(Dispatchers.IO) {
            try {


                val response =
                    firebaseDataBase.child("All Users").child(uid).child("Books").get().await()
                for (i in response.children) {
                    val book = i.getValue(Book::class.java)
                    //if (book!!.key == key) {
                        firebaseDataBase.child("All Users").child(uid).child("Books").child(i.key!!)
                            .removeValue().await()
                    //}

                }
            }catch (e:Exception){
                theReturn=false;

            }
        }
        val job3=viewModelScope.launch (Dispatchers.IO){

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