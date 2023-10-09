package com.example.bookexchange.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainPageViewModel: ViewModel() {


    var count=MutableLiveData<Int>()

    fun readSize(uid:String){

        val firebaseDatabase=FirebaseDatabase.getInstance().reference
        firebaseDatabase.child("All Users").child(uid).child("Requests").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                viewModelScope.launch(Dispatchers.IO) {
                    var _count = 0;
                    for (i in snapshot.children) {

                        val seen=i.child("seen").getValue(Boolean::class.java)

                        if(seen==false)
                            _count++;
                        AppUtils.LOG(i.toString());
                    }
                    count.postValue(_count)

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}