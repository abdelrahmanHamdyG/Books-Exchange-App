package com.example.bookexchange.Models

class RequestWithBooks {

    var rid:Int?=null
    var uid1:String?=null;
    var uid2:String?=null;
    var myBooks:ArrayList<Book>?=null;
    var hisBooks:ArrayList<Book>?=null;
    var state:String?=null;
    var clicked:Boolean?=null;
    var date:String?=null

    constructor(){

    }
    constructor(uid1:String,
                uid2:String,
                myBooks:ArrayList<Book>,
                hisBooks:ArrayList<Book>,
                state:String,
                clicked:Boolean,
                date:  String
    ){
        rid=501;
        this.uid1=uid1
        this.uid2=uid2
        this.myBooks=myBooks
        this.hisBooks=hisBooks
        this.state=state
        this.clicked=clicked
        this.date=date;
    }
    constructor(
        rid:Int, uid1:String,
        uid2:String,
        myBooks: ArrayList<Book>,
        hisBooks: ArrayList<Book>,
        state:String,
        clicked:Boolean,
        date:  String
    ){
        this.rid=rid;
        this.uid1=uid1
        this.uid2=uid2
        this.myBooks=myBooks
        this.hisBooks=hisBooks
        this.state=state
        this.clicked=clicked
        this.date=date;
    }


    constructor( uid1:String,uid2:String,
                 state:String,clicked:Boolean,date:String
    ){
        this.uid1=uid1
        this.uid2=uid2

        this.state=state
        this.clicked=clicked
        this.date=date;
    }

}