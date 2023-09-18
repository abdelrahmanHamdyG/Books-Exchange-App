package com.example.bookexchange.Models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserData {

    var name: String? =null
    var email: String? =null
    var password: String? =null
    var governorate: String? =null
    var detailed: String? =null
    constructor(){

    }
    constructor(name:String,email:String,password: String,governorate: String,detailed: String){
        this.name=name
        this.email=email
        this.password=password
        this.governorate=governorate
        this.detailed=detailed

    }


    fun readUserData(uid:String){


        var firebaseDatabase=FirebaseDatabase.getInstance().getReference()

        firebaseDatabase.child("All Users").child(uid).child("DATA").addListenerForSingleValueEvent(object:
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if(!snapshot.exists()){

                    Log.i("my_trag","it is null")
                }else {

                    Log.i("my_trag", "On Data Changed")
                    val userData = snapshot.getValue(UserData::class.java)

                    governorate = userData?.governorate.toString()
                    detailed=userData?.detailed.toString()

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("my_trag", "On cancelled$error")

            }
        })





    }
}

