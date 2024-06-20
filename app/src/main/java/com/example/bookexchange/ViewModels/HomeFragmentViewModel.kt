package com.example.bookexchange.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bookexchange.Adapters.OnItemClickListener
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class HomeFragmentViewModel:ViewModel() {


    var _myBooksList=ArrayList<Book>()
    var filteredBooksList=MutableLiveData<ArrayList<Book>>()
    var _filteredBooksList=ArrayList<Book>()

    suspend fun readAllBooks(uid:String,flag:Int,query:String){

        _myBooksList.clear()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit= Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()

        val api =retrofit.create(ApiInterface::class.java)

        try{
            if(flag==1) {
                val books = api.getAllBooks(uid).await()


                _myBooksList.addAll(books)
            }else{

                val books = api.getBookBySearch(uid,query).await()
                _myBooksList.addAll(books)

            }

        }catch (e:Exception){

            Log.e("API_CALL", "API call failed: ${e.message}")
        }

        /*

        try {


            val snapshot = firebaseDatabase.child("AllBooks").get().await()
            for (book in snapshot!!.children) {
                Log.i("my_trag", book.toString())
                val bookName = book.child("bookName").getValue(String::class.java)
                val bookDescription = book.child("bookDescription").getValue(String::class.java)
                val category = book.child("category").getValue(String::class.java)
                val imageUri = book.child("imageUri").getValue(String::class.java)
                val user = book.child("user").getValue(String::class.java)
                val key = book.child("key").getValue(String::class.java)
                val city = book.child("city").getValue(String::class.java)
                val state= book.child("state").getValue(String::class.java)

                if (user == uid) {
                    continue;
                }


                _myBooksList.add(
                    Book(
                        bookName!!,
                        bookDescription!!,
                        category!!,
                        imageUri!!,
                        user!!,
                        state!!
                    )
                )
            }
            filteredBooksList.postValue(_myBooksList)
        }catch (e:Exception){

        }
        */


        filteredBooksList.postValue(_myBooksList)


    }

    suspend fun filterByCategory(mask: Array<Int> ){
        if(mask[0]==1) {
            filteredBooksList.postValue(_myBooksList)
            return
        }

        _filteredBooksList.clear()
        val textToBooleanMap = mutableMapOf<String, Boolean>()

        for(i in 1 until mask.size){
            textToBooleanMap[AppUtils.texts[i]] = mask[i]==1
        }
        for (i in _myBooksList){

            if(textToBooleanMap[i.category] == true){
                _filteredBooksList.add(i)
            }
        }

        filteredBooksList.postValue(_filteredBooksList)
    }

    suspend fun filterByFavourite(uid:String ){

        _filteredBooksList.clear()
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        val retrofit= Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()

        val api =retrofit.create(ApiInterface::class.java)



        api.getFavouriteBooks(uid).enqueue(object: Callback<List<Book>> {
            override fun onResponse(call: Call<List<Book>>, response: Response<List<Book>>) {
                Log.e("response ", "favourites or ${response.body()} ")

                _filteredBooksList.addAll(response.body()!!)
                filteredBooksList.postValue(_filteredBooksList)
            }

            override fun onFailure(call: Call<List<Book>>, t: Throwable) {
                Log.e("response ", "baadd ${t.message} ")
            }


        })

        /*
        if(mask[0]==1) {
            filteredBooksList.postValue(_myBooksList)
            return
        }

        _filteredBooksList.clear()
        val textToBooleanMap = mutableMapOf<String, Boolean>()

        for(i in 1 until mask.size){
            textToBooleanMap[AppUtils.texts[i]] = mask[i]==1
        }
        for (i in _myBooksList){

            if(textToBooleanMap[i.category] == true){
                _filteredBooksList.add(i)
            }
        }

         */

    }



}