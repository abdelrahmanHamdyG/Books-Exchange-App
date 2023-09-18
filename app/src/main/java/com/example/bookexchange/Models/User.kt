package com.example.bookexchange.Models

import android.util.Log

class User {

    var uid:String?=null;
    var userData: UserData?=null;
    var books:ArrayList<Book>?=null;

    private constructor(){

    }

    companion object{

        private var instance: User?=null;
        fun getInstance(): User? {

            if(instance !=null)
                return instance!!;
            else {
                Log.i("my_trag","null instance")
                return null;
            }
        }

        fun getInstance(uid:String): User {

            if(instance ==null) {
                instance = User();
                instance!!.uid=uid;
                instance!!.readData()
            }
            return instance!!;

        }

    }

    fun readData(){



    }



}