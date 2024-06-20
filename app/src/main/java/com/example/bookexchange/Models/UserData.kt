package com.example.bookexchange.Models

import android.provider.ContactsContract.CommonDataKinds.Phone
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserData {

    var uid:String?=null
    var fname: String? =null
    var lname:String?=null;
    var email: String? =null
    var pass: String? =null
    var governorate: String? =null
    var phone_num:String?=null
    var detailed_address: String? =null
    constructor(){

    }
    constructor(fname:String,lname:String,email:String,pass: String,governorate: String,detailed_address: String,phone_num: String){
        uid="dummy"
        this.fname=fname
        this.lname=lname
        this.email=email
        this.pass=pass
        this.governorate=governorate
        this.detailed_address=detailed_address
        this.phone_num=phone_num

    }



}

