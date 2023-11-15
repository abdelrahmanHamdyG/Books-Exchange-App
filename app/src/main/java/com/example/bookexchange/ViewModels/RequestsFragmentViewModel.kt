package com.example.bookexchange.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Request
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RequestsFragmentViewModel: ViewModel() {




    var _requests=ArrayList<Request>()
    var requests=MutableLiveData<ArrayList<Request>>()

    suspend fun makeEveryThingSeen(uid:String){
        val firebaseDatabase= FirebaseDatabase.getInstance()
        val reference=firebaseDatabase.reference.child("All Users").child(uid).child("Requests")

        val query=reference.orderByChild("seen").equalTo(false);


        try {
            val dataSnapshot = query.get().await()

            for (snapshot in dataSnapshot.children) {
                val request = snapshot.getValue(Request::class.java)
                if (request != null) {

                    request.seen = true


                    snapshot.ref.setValue(request).await()
                }
            }
        } catch (e: Exception) {
            AppUtils.LOG("Clear Requests Exception $e")
        }
    }


    fun readRequests(uid:String){

        val firebaseDatabase= FirebaseDatabase.getInstance()
        val reference=firebaseDatabase.reference.child("All Users").child(uid).child("Requests")

        reference.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {


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


    }



}