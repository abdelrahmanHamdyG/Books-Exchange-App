package com.example.bookexchange.Models

import android.provider.ContactsContract.CommonDataKinds.Phone
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
    var phone:String?=null
    var detailed: String? =null
    constructor(){

    }
    constructor(name:String,email:String,password: String,governorate: String,detailed: String,phone: String){
        this.name=name
        this.email=email
        this.password=password
        this.governorate=governorate
        this.detailed=detailed
        this.phone=phone

    }



}

