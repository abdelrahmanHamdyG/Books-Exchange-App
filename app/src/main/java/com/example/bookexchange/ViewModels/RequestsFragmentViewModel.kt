package com.example.bookexchange.ViewModels

import androidx.lifecycle.ViewModel
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Request
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class RequestsFragmentViewModel: ViewModel() {



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
}