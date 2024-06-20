package com.example.bookexchange


import retrofit2.Call
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.Request
import com.example.bookexchange.Models.UserData
import com.google.android.gms.common.api.Api.Client
import retrofit2.http.*

interface ApiInterface {


    @POST("addUser")
    fun addUser(@Body user: UserData):Call<String>;

    @POST("mybooks/{uid}")
    fun addBook(@Body book:Book,@Path("uid") uid:String):Call<String>

    @PUT("mybooks/{bid}")
    fun updateBook(@Body book:Book,@Path("bid") bid: Int?):Call<String>

    @DELETE("mybooks/{bid}")
    fun deleteBook(@Path("bid") uid: Int?):Call<String>

    @GET("mybooks/{uid}")
    fun getMyBooks(@Path("uid") uid:String):Call<List<Book>>

    @GET("books/all/{uid}")
    fun getAllBooks(@Path("uid")uid:String):Call<List<Book>>

    @POST("requests/addRequest")
    fun makeRequest(@Body request:Request):Call<String>


    @GET("requests/{uid}")
    fun getMyRequests(@Path("uid") uid:String?):Call<List<Request>>



    @GET("oneRequest/{rid}")
    fun getARequest(@Path("rid") rid:Int?):Call<List<Request>>


    @GET("getBooksDecision/{cats}")
    fun getBooksDecision(@Path("cats") cats:String):Call<List<Book>>

    @GET("requestMyBooks/{rid}&{uid}")
    fun getMyBooksR(
        @Path("rid") requestId: Int,
        @Path("uid") userId: String):Call<List<Book>>


    @GET("requestOtherBooks/{rid}&{uid}")
    fun getOtherBooks(
        @Path("rid") requestId: Int,
        @Path("uid") userId: String):Call<List<Book>>


    @PUT("decline/{rid}")
    fun acceptRequest(@Path("rid") requestId: Int):Call<String>

    @GET("clients/{uid}")
    fun getAClient(@Path("uid") uid: String):Call<List<UserData>>;

    @GET("search/{uid}&{Kword}")
    fun getBookBySearch(@Path ("uid") uid:String,@Path("Kword") Kword:String):Call<List<Book>>


    @GET("checkFavourite/{uid1}/{uid2}")
    fun checkFavourite(@Path ("uid1") uid1:String,@Path ("uid2") uid2:String):Call<List<Favourites>>



    @POST("favourite/{uid1}&{uid2}")
    fun addFavourite(@Path ("uid1") uid1:String,@Path ("uid2") uid2:String):Call<String>


    @DELETE("favourite/{uid1}&{uid2}")
    fun removeFavourite(@Path ("uid1") uid1:String,@Path ("uid2") uid2:String):Call<String>


    @GET("favourite/{uid1}")
    fun getFavouriteBooks(@Path ("uid1") uid1:String):Call<List<Book>>


    @POST("declineRequest/{rid}")
    fun declineRequest(@Path ("rid") rid:Int):Call<String>







}