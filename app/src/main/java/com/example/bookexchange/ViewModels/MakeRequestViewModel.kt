package com.example.bookexchange.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.Request
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MakeRequestViewModel: ViewModel() {



    var result= MutableLiveData<Boolean>()


    fun makeRequest(myBooks:ArrayList<Book>,hisBooks:ArrayList<Book>,myKey:String,hisKey:String){

        viewModelScope.launch(Dispatchers.IO){

            val first=async { uploadToMe(myBooks,hisBooks,myKey,hisKey) }
            val second=async { uploadToHim(myBooks,hisBooks,myKey,hisKey)  }


            if(first.await()&&second.await()){

                result.postValue(true);
            }else{

                result.postValue(false);
            }
        }

    }




    suspend fun uploadToMe(myBooks:ArrayList<Book>, hisBooks:ArrayList<Book>, myKey:String, hisKey:String): Boolean {

        var  check=true;
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference


            val request = Request(myKey, hisKey, true, true, myBooks, hisBooks, "Sent",false,
                System.currentTimeMillis().toInt().toLong() );


            try {
                firebaseDatabase.child("All Users").child(myKey).child("Requests").child(myKey + hisKey)
                    .setValue(request)
                    .await()

            } catch (e: Exception) {

                check=false;
            }

        }
        job.join()
        return check
    }

    suspend fun uploadToHim(myBooks:ArrayList<Book>, hisBooks:ArrayList<Book>, myKey:String, hisKey:String): Boolean {

        var  check=true;
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference

            val request = Request( hisKey,myKey, false, false,  hisBooks,myBooks, "Received",false,
                System.currentTimeMillis().toLong() );

            try {
                firebaseDatabase.child("All Users").child(hisKey).child("Requests").child(hisKey + myKey)
                    .setValue(request)
                    .await()

            } catch (e: Exception) {

                check=false;
            }

        }
        job.join()
        return check

    }


}