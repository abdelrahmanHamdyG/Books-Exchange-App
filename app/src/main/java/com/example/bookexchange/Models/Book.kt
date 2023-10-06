package com.example.bookexchange.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Book() :Parcelable{
    var bookName: String? =null
    var bookDescription:String? =null
    var category:String? =null
    var imageUri:String? =null
    var city:String?=null
    var user:String? =null
    var key:String?=null
    var state:String?=null

    constructor(bookName:String,bookDescription:String,category: String,imageUri: String,user: String,key:String,city:String,state:String) : this() {
        this.bookName=bookName
        this.bookDescription=bookDescription
        this.category=category
        this.imageUri=imageUri
        this.user=user
        this.key=key
        this.city=city
        this.state=state
    }

}
