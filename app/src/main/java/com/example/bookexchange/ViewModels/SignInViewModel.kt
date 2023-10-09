package com.example.bookexchange.ViewModels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel:ViewModel() {

    var loginResult= MutableLiveData<String>();
    lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    fun login(email:String, password:String){
        firebaseAuth=FirebaseAuth.getInstance();
            viewModelScope.launch (Dispatchers.IO) {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {

                        loginResult.value = "Success"
                    } else {
                        loginResult.value = it.exception!!.message
                    }

                }
            }

    }
}