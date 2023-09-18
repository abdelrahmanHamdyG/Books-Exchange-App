package com.example.bookexchange.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*

import kotlinx.coroutines.tasks.await

class AddBookViewModel:ViewModel() {


    var result=MutableLiveData<Boolean>()


    fun uploadBookAndTheImage(uid:String,book:Book,imageByteArray: ByteArray){
        viewModelScope.launch(Dispatchers.IO){

            val first=async { uploadTheBook(book,uid) }
            val second=async { uploadImage(uid,imageByteArray,book.imageUri!!) }

            if(first.await()&&second.await()){
                AppUtils.LOG("what is happening #win")
                result.postValue(true)
            }else{
                result.postValue(false)
            }


        }


    }

    suspend fun uploadTheBook(book: Book,uid:String):Boolean{


        var f=false;
        var job=viewModelScope.launch (Dispatchers.IO){
            val city=getCityName(uid)

            book.city=city

            val first= uploadToAllBooks(book,uid)

            book.key=first;

            val second=async { uploadToUserBooks(book,uid) }

            val resultSecond = second.await()
            AppUtils.LOG("the first ${book.key} the second $resultSecond")
            if(first!="null"&&resultSecond)
                f=true;

        }
        AppUtils.LOG("upload the book:$f")
        job.join()
        return f;

    }



    suspend fun uploadToAllBooks(book:Book,uid:String):String{
        AppUtils.LOG("upload the book to AllBooks current thread is ${Thread.currentThread().name}")


            try {
                val firebaseDatabase=FirebaseDatabase.getInstance()
                val key=firebaseDatabase.reference.child("AllBooks").push().key;
                book.key=key;
                firebaseDatabase.reference.child("AllBooks").child(key.toString()).setValue(book).await()

            } catch (e:Exception) {
                AppUtils.LOG("upload toAllBooks:${e.message}")
                return "null";
            }
        return book.key!!;

    }
    suspend fun uploadToUserBooks(book:Book,uid:String):Boolean{
        AppUtils.LOG("upload the book current thread is ${Thread.currentThread().name}")
        val firebaseDatabase = FirebaseDatabase.getInstance().reference

        try {
            firebaseDatabase.child("All Users").child(uid).child("Books").push().setValue(book).await()

        } catch (e: Exception) {

            AppUtils.LOG("upload toUserBooks:${e.message}")
            return false;
        }
        return true;


    }




    suspend fun uploadImage(uid:String,imageByteArray: ByteArray,imageName:String):Boolean{
        AppUtils.LOG("upload the image current thread is ${Thread.currentThread().name}")
        val firebaseStorage=FirebaseStorage.getInstance().reference
        try {
            firebaseStorage.child(imageName).putBytes(imageByteArray).await()

        }catch (e:Exception){
            AppUtils.LOG("upload the image problem ${e.message} ")
            return false;
        }

        return true;
    }



    suspend fun getCityName(uid:String): String {

        val firebaseDatabase= FirebaseDatabase.getInstance().reference

        var city="null";

        try {
            var data = firebaseDatabase.child("All Users").child(uid).child("DATA").get().await()

            if(data.exists()){

                city=data.getValue(UserData::class.java)!!.governorate!!;
            }

        }catch (e:Exception){


        }
        AppUtils.LOG(city)
        return city;

    }

}