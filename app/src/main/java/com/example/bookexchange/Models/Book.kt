package com.example.bookexchange.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Book() :Parcelable{
    var bid:Int?=null
    var title: String? =null
    var description:String? =null
    var image_link:String? =null
    var category:String?=null
    var bstate:String?=null
    var uid:String? =null


    constructor(title:String,description:String,category: String,image_link: String,bstate :String,uid: String) : this() {
        this.title=title
        this.description=description
        this.category=category
        this.image_link=image_link
        this.uid=uid
        this.bstate=bstate
    }

    constructor(bid:Int,title:String,description:String,category: String,image_link: String,uid: String,bstate:String) : this() {
        this.bid=bid
        this.title=title
        this.description=description
        this.category=category
        this.image_link=image_link
        this.uid=uid
        this.bstate=bstate
    }

}
