package com.example.bookexchange.ViewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class SignInViewModel:ViewModel() {

    var loginResult= MutableLiveData<String>();
    lateinit var firebaseAuth: FirebaseAuth
    var loginRunning=false;

    @SuppressLint("SuspiciousIndentation")
    suspend fun login(email:String, password:String){
        firebaseAuth=FirebaseAuth.getInstance();

        var timeoutEnded=false;

         try {
             withTimeout(5000) {
                 firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                     if (it.isSuccessful) {

                         if(!timeoutEnded){

                         loginResult.postValue("Success")
                         }else{
                             firebaseAuth.signOut()
                         }
                     } else {
                         if(!timeoutEnded)
                         loginResult.postValue(it.exception!!.message)
                     }

                 }.await()

             }
         }catch (e:TimeoutCancellationException){
             AppUtils.LOG("somthing is happening")
             loginResult.postValue("timeout")
             timeoutEnded=true;

         }
    }
}