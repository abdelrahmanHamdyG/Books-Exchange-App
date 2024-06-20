package com.example.bookexchange.ViewModels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*

import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream

class AddBookViewModel:ViewModel() {


    var result=MutableLiveData<Boolean>()




    fun uploadBookAndTheImage(uid:String,book:Book,imageByteArray: ByteArray,flag:Int){
        viewModelScope.launch(Dispatchers.IO){

            Log.i("book is ",book.image_link!!)
            Log.i("book is ",book.title!!)

            uploadToUserBooks(book,uid,flag)
            uploadImage(uid,imageByteArray,book.image_link!!)

            result.postValue(true)

        }

    }


     private fun uploadToUserBooks(book:Book,uid:String,flag:Int){


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

         if(flag==1) {
             val api = retrofit.create(ApiInterface::class.java)

             val call = api.addBook(book, uid);

             call.enqueue(object : Callback<String> {
                 override fun onResponse(call: Call<String>, response: Response<String>) {
                     Log.e("API_CALL", response.toString());
                     if (response.isSuccessful) {
                         val message = response.body()
                         Log.i("API_CALL Success", message.toString())

                     } else {

                         Log.i("API_CALL no Success", response.body().toString())

                     }
                 }

                 override fun onFailure(call: Call<String>, t: Throwable) {
                     Log.e("API_CALL", "API call failed: ${t.message}")
                     Log.e("API_CALL", "it is failed ya 7omar")
                     // Handle the failure here
                 }
             })
         }else{


             Log.i("book is ",book.image_link!!)
             Log.i("bookbid is ", book.bid!!.toString())


             val api = retrofit.create(ApiInterface::class.java)

             val call = api.updateBook(book, book.bid);

             call.enqueue(object : Callback<String> {
                 override fun onResponse(call: Call<String>, response: Response<String>) {
                     Log.e("API_CALL", response.toString());
                     if (response.isSuccessful) {
                         val message = response.body()
                         Log.i("API_CALL Success", message.toString())

                     } else {

                         Log.i("API_CALL no Success", response.body().toString())

                     }
                 }

                 override fun onFailure(call: Call<String>, t: Throwable) {
                     Log.e("API_CALL", "API call failed: ${t.message}")
                     Log.e("API_CALL", "it is failed ya 7omar")
                     // Handle the failure here
                 }
             })

         }
        /*
        val firebaseDatabase = FirebaseDatabase.getInstance().reference

        try {
            firebaseDatabase.child("All Users").child(uid).child("Books").push().setValue(book).await()

        } catch (e: Exception) {


            return false;
        }
        return true;
        */

    }




    private  fun uploadImage(uid:String, imageByteArray: ByteArray, imageName:String){
        val firebaseStorage=FirebaseStorage.getInstance().reference
        firebaseStorage.child(imageName).putBytes(imageByteArray)

    }




}