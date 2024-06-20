package com.example.bookexchange.Models

class Request {

    var rid:Int?=null
    var uid1:String?=null;
    var uid2:String?=null;
    var myBooks:ArrayList<Int>?=null;
    var hisBooks:ArrayList<Int>?=null;
    var rstate:String?=null;
    var clicked:Boolean?=null;
    var rdate:String?=null

    constructor(){

    }
        constructor(uid1:String,
                uid2:String,
                myBooks:ArrayList<Int>,
                hisBooks:ArrayList<Int>,
                rstate:String,
                clicked:Boolean,
                rdate:  String
    ){
        rid=501;
       this.uid1=uid1
        this.uid2=uid2
        this.myBooks=myBooks
        this.hisBooks=hisBooks
        this.rstate=rstate
        this.clicked=clicked
        this.rdate=rdate;
    }

    constructor( uid1:String,uid2:String,
                 rstate:String,clicked:Boolean,rdate:String
    ){
        this.uid1=uid1
        this.uid2=uid2

        this.rstate=rstate
        this.clicked=clicked
        this.rdate=rdate;
    }

}