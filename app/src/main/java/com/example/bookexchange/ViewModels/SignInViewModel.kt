package com.example.bookexchange.ViewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookexchange.AppUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class SignInViewModel:ViewModel() {

    var loginResult= MutableLiveData<String>();
    lateinit var firebaseAuth: FirebaseAuth
    var loginRunning=false;



    suspend fun login(email:String, password:String){
        firebaseAuth=FirebaseAuth.getInstance();

        var timeoutEnded=false;

         try {
             withTimeout(8000) {
                 try {


                     firebaseAuth.signInWithEmailAndPassword(email, password)
                         .addOnCompleteListener {
                             if (it.isSuccessful) {


                                 if (!timeoutEnded) {

                                     loginResult.postValue("Success")
                                 } else {

                                     firebaseAuth.signOut()
                                 }
                             }

                         }.await()
                 }catch (e:FirebaseAuthException){
                        AppUtils.LOG("general exception is ${e.message.toString()} ")
                        if(!timeoutEnded) {
                            loginResult.postValue(e.message.toString())
                        }else{

                        }
                 }
             }
         }catch (e:TimeoutCancellationException){

             loginResult.postValue("timeout")
             timeoutEnded=true;

         }
    }

    override fun onCleared() {
        super.onCleared()
        AppUtils.LOG("the signInViewModel is cleared")
    }
}