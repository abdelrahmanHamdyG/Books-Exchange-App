package com.example.bookexchange.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.Models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class RegisterationViewModel: ViewModel() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    var registerResult=MutableLiveData<String>()
     fun registerAndWriteData(userData: UserData){
        viewModelScope.launch(Dispatchers.IO) {

            val uid=register(
                userData.email!!,
                userData.pass!!
            )

            if(uid!="null"){
                userData.uid=uid;
                writeData(uid, userData)
            }

        }

    }

    private fun writeData(uid:String, userData: UserData){
/*
        firebaseDatabase=FirebaseDatabase.getInstance()
        val reference=firebaseDatabase.reference
        reference.child("All Users").child(uid).child("DATA").setValue(userData).addOnCompleteListener {
            if(!it.isSuccessful){

                registerResult.value=it.exception!!.message.toString()
            }else{

                registerResult.value="Success"
            }


        };
*/
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit=Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()


        val api =retrofit.create(ApiInterface::class.java)

        val call=api.addUser(userData)

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.e("API_CALL",response.body().toString());
                if (response.isSuccessful) {
                    val message = response.body()
                    Log.i("API_CALL",message.toString())
                    registerResult.value="Success"

                } else {

                    registerResult.value=response.errorBody().toString()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("API_CALL", "API call failed: ${t.message}")
                Log.e("API_CALL","it is failed ya 7omar")
                // Handle the failure here
            }
        })

    }

     private suspend fun register(email:String, password: String): String {


         try {
             firebaseAuth=FirebaseAuth.getInstance()
             val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
             if (authResult.user != null) {
                 return authResult.user!!.uid
             } else {
                 return "null"
             }
         } catch (e: Exception) {
             // Handle exceptions here

             registerResult.postValue(e.message.toString())
             return "null"
         }




     }

}