package com.example.bookexchange.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.Request
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MakeRequestViewModel: ViewModel() {



    var result= MutableLiveData<Boolean>()


    fun makeRequest(myBooks:ArrayList<Int>,hisBooks:ArrayList<Int>,myKey:String,hisKey:String){
        val time=System.currentTimeMillis()
        val request = Request(myKey, hisKey,  myBooks, hisBooks, "pending",false,
            time.toString() );


        viewModelScope.launch(Dispatchers.IO){

            val gson = GsonBuilder()
                .setLenient()
                .create()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)


            try{

                val retrofit=Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build()


                val api =retrofit.create(ApiInterface::class.java)

                api.makeRequest(request).await()
                result.postValue(true)


            }catch (e:Exception){

                Log.e("API_CALL", "API call failed: ${e.message}")
                result.postValue(false);
            }

        }

    }



/*
    suspend fun uploadToMe(myBooks:ArrayList<Int>, hisBooks:ArrayList<Int>, myKey:String, hisKey:String,time:Long): Boolean {

        var  check=true;
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference


            val request = Request(myKey, hisKey,  myBooks, hisBooks, "Pending",false,
                time );


            try {
                /*
                firebaseDatabase.child("All Users").child(myKey).child("Requests").child(myKey + hisKey)
                    .setValue(request)
                    .await()
                    */
            } catch (e: Exception) {

                check=false;
            }

        }
        job.join()
        return check
    }
        */

    /*
    suspend fun uploadToHim(myBooks:ArrayList<Book>, hisBooks:ArrayList<Book>, myKey:String, hisKey:String,time:Long): Boolean {

        var  check=true;
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference

            val request = Request( hisKey,myKey, false, false,  hisBooks,myBooks, "Received",false,
              time);

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
*/

}