package com.example.bookexchange.Models

class Request {


    var myKey:String?=null;
    var hisKey:String?=null;
    var fromMe:Boolean?=null;
    var seen:Boolean?=null;
    var myBooks:ArrayList<Book>?=null;
    var hisBooks:ArrayList<Book>?=null;
    var state:String?=null;
    var clicked:Boolean?=null;
    var date:Int?=null

    constructor(){

    }
    constructor( myKey:String,hisKey:String,fromMe:Boolean,seen:Boolean,myBooks:ArrayList<Book>,hisBooks:ArrayList<Book>,
    state:String,clicked:Boolean,date:Int
    ){
       this.myKey=myKey
        this.hisKey=hisKey
        this.fromMe=fromMe
        this.seen=seen
        this.myBooks=myBooks
        this.hisBooks=hisBooks
        this.state=state
        this.clicked=clicked
        this.date=date;
    }

}