package com.example.bookexchange.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.Models.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class RegisterationViewModel: ViewModel() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    var registerResult=MutableLiveData<String>()
     fun registerAndWriteData(userData: UserData){
        viewModelScope.launch(Dispatchers.IO) {

            val uid=register(
                userData.email!!,
                userData.password!!
            )

            if(uid!="null"){
                writeData(uid, userData)

            }

        }

    }

    private  fun writeData(uid:String, userData: UserData){


        firebaseDatabase=FirebaseDatabase.getInstance()
        val reference=firebaseDatabase.reference
        reference.child("All Users").child(uid).child("DATA").setValue(userData).addOnCompleteListener {
            if(!it.isSuccessful){

                registerResult.value=it.exception!!.message.toString()
            }else{

                registerResult.value="Success"
            }


        };



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