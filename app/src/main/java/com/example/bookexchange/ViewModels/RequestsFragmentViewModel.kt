package com.example.bookexchange.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Request
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RequestsFragmentViewModel: ViewModel() {




    var _requests=ArrayList<Request>()
    var requests=MutableLiveData<ArrayList<Request>>()

     fun makeEveryThingSeen(uid:String){
        val firebaseDatabase= FirebaseDatabase.getInstance()
        val reference=firebaseDatabase.reference.child("All Users").child(uid).child("Requests")

        val query=reference.orderByChild("seen").equalTo(false);


        try {
            viewModelScope.launch {
            val dataSnapshot = query.get().await()

            for (snapshot in dataSnapshot.children) {
                val request = snapshot.getValue(Request::class.java)
                if (request != null) {

                    //request.seen = true


                        snapshot.ref.setValue(request).await()
                    }
                }
            }
        } catch (e: Exception) {
            AppUtils.LOG("Clear Requests Exception $e")
        }
    }


     fun readRequests(uid:String){


         _requests.clear()

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


        AppUtils.LOG("RequestsFragmentViewModel:ReadRequests")



        try{

            viewModelScope.launch {
                val api = retrofit.create(ApiInterface::class.java)

                val requestss = api.getMyRequests(uid!!).await()
                Log.i("TAG",requestss.size.toString())
                _requests.addAll(requestss)

                requests.postValue(_requests)
            }

        }catch (e:Exception){

                Log.e("exception",e.message.toString());

        }

        /*
        val firebaseDatabase= FirebaseDatabase.getInstance()
        val reference=firebaseDatabase.reference.child("All Users").child(uid).child("Requests")

        reference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                AppUtils.LOG("RequestsFragmentViewModel:ReadRequests:OnDataChange triggered")
                _requests.clear()

                    for (i in snapshot.children) {

                        val item = i.getValue(Request::class.java)
                        _requests.add(item!!)
                    }



                    requests.postValue(_requests)

            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

            */
    }



}