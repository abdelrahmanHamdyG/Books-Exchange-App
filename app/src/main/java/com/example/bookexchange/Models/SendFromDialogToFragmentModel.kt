package com.example.bookexchange.Models

class SendFromDialogToFragmentModel {

    var position:Int?=null;
    var byteArray:ByteArray?=null

    constructor(){

    }
    constructor(position:Int,byteArray: ByteArray){
        this.position=position
        this.byteArray=byteArray

    }


}