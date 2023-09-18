package com.example.bookexchange.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
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

            var uid=register(userData.email!!, userData.password!!)
            AppUtils.LOG("the uid is $uid and the current thread is ${Thread.currentThread().name}")
            if(uid!="null"){
                writeData(uid, userData);

            }

        }

    }

    private suspend fun writeData(uid:String, userData: UserData){

        AppUtils.LOG("Registeration ViewModel:writeData current thread is ${Thread.currentThread().name}")
        firebaseDatabase=FirebaseDatabase.getInstance()
        var reference=firebaseDatabase.reference
        reference.child("All Users").child(uid).child("DATA").setValue(userData).addOnCompleteListener {
            if(!it.isSuccessful){

                registerResult.value=it.exception!!.message.toString();
            }else{

                registerResult.value="Success";
            }


        };



    }

     private suspend fun register(email:String, password: String): String {
         AppUtils.LOG("Registeration ViewModel:register current thread is  ${Thread.currentThread().name}")

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
             AppUtils.LOG("Failed  ${Thread.currentThread().name}")
             registerResult.postValue(e.message.toString())
             return "null"
         }




     }

}