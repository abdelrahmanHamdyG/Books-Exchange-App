package com.example.bookexchange.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookexchange.Adapters.OnItemClickListener
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class HomeFragmentViewModel:ViewModel() {


    var _myBooksList=ArrayList<Book>()
    var filteredBooksList=MutableLiveData<ArrayList<Book>>()
    var _filteredBooksList=ArrayList<Book>()

    suspend fun readAllBooks(uid:String){

        val firebaseDatabase=FirebaseDatabase.getInstance().reference

        try {


            val snapshot = firebaseDatabase.child("AllBooks").get().await()
            for (book in snapshot!!.children) {
                Log.i("my_trag", book.toString())
                val bookName = book.child("bookName").getValue(String::class.java)
                val bookDescription = book.child("bookDescription").getValue(String::class.java)
                val category = book.child("category").getValue(String::class.java)
                val imageUri = book.child("imageUri").getValue(String::class.java)
                val user = book.child("user").getValue(String::class.java)
                val key = book.child("key").getValue(String::class.java)
                val city = book.child("city").getValue(String::class.java)
                val state= book.child("state").getValue(String::class.java)

                if (user == uid) {
                    continue;
                }


                _myBooksList.add(
                    Book(
                        bookName!!,
                        bookDescription!!,
                        category!!,
                        imageUri!!,
                        user!!,
                        key!!,
                        city!!,
                        state!!
                    )
                )
            }
            filteredBooksList.postValue(_myBooksList)
        }catch (e:Exception){

        }



    }

    suspend fun filterByCategory(mask: Array<Int> ){
        if(mask[0]==1) {
            filteredBooksList.postValue(_myBooksList)
            return
        }

        _filteredBooksList.clear()
        val textToBooleanMap = mutableMapOf<String, Boolean>()

        for(i in 1 until mask.size){
            textToBooleanMap[AppUtils.texts[i]] = mask[i]==1
        }
        for (i in _myBooksList){

            if(textToBooleanMap[i.category] == true){
                _filteredBooksList.add(i)
            }
        }

        filteredBooksList.postValue(_filteredBooksList)
    }



}