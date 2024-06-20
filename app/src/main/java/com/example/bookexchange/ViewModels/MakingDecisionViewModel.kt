package com.example.bookexchange.ViewModels

import android.appwidget.AppWidgetProvider
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookexchange.Models.Request
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.RequestWithBooks
import com.example.bookexchange.Models.SendFromDialogToFragmentModel

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.internal.applyConnectionSpec
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


fun contain(books:ArrayList<Book>, book:Book):Boolean{

    for(i in books) {
        if (i.title==book.title &&i.description==book.description)
            return true
    }
    return false
}
class MakingDecisionViewModel: ViewModel() {


    var request= MutableLiveData<RequestWithBooks>()

    var numberOfReadRequest=0

    var result= MutableLiveData<Boolean>()



    suspend fun readTheRequest(uid:String, rid:Int) {


        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)


        val retrofit = Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()


        AppUtils.LOG("RequestsFragmentViewModel:ReadRequests")




            val api = retrofit.create(ApiInterface::class.java)



                val req = api.getARequest(rid).await()[0]
                Log.i("TRAAG", req.uid1.toString())
                Log.i("TRAAG", req.uid2.toString())
                Log.i("TRAAG", req.rid.toString())


                val myBooks=api.getMyBooksR(rid,uid).await()
                val hisBooks=api.getOtherBooks(rid,uid).await()
                Log.i("TRAAG",myBooks[0].title!!)
                Log.i("TRAAG",hisBooks[0].title!!)
                val lmyBooks = ArrayList(myBooks);
                val lHisBooks = ArrayList(hisBooks)
                val requestWithBooks=RequestWithBooks(req.rid!!,req.uid1!!,req.uid2!!,
                    lmyBooks!! ,
                    lHisBooks!!,
                    req.rstate!!,
                    req.clicked!!,
                    "202248597")
                request.postValue(requestWithBooks)







    }




    suspend fun readTheBooksImages(arr:ArrayList<Book>):ArrayList<SendFromDialogToFragmentModel >{


        val firebaseStorage=FirebaseStorage.getInstance().reference

        val books=ArrayList<SendFromDialogToFragmentModel>()

        var count=0;

        for( i in arr) {

            AppUtils.LOG(" the image is number $count")
            try {

                AppUtils.LOG(" the image uri is ${i.image_link}")
                val imageAsByte=firebaseStorage.child(i.image_link.toString()).getBytes(4096 * 4096).await()

                books.add(SendFromDialogToFragmentModel(count,imageAsByte))

                count++;
            }catch (e:Exception){
                AppUtils.LOG("Error ${e.message}")

            }

        }

        numberOfReadRequest++;
        if(numberOfReadRequest==3)
            numberOfReadRequest=1;

        return books;
    }


    fun cancelRequest(rid:Int){

        viewModelScope.launch(Dispatchers.IO){
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)


            val retrofit = Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()


            AppUtils.LOG("RequestsFragmentViewModel:ReadRequests")

            val api = retrofit.create(ApiInterface::class.java)

            try {

                api.declineRequest(rid).enqueue(object :Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        Log.e("errrror",response.body().toString())
                        result.postValue(true)
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("error ",t.message.toString())
                        result.postValue(false)
                    }
                });
            } catch(e:Exception){

                Log.e("excccccc",e.message.toString())

            }


        }
    }




    fun acceptTheRequest(myKey: String, rid: Int){


        val gson = GsonBuilder()
            .setLenient()
            .create()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)


        val retrofit = Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()


        AppUtils.LOG("RequestsFragmentViewModel:ReadRequests")

        val api = retrofit.create(ApiInterface::class.java)

        try {

            api.acceptRequest(rid).enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {

                    Log.e("errrror",response.body().toString())
                    //result.postValue(true)
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("error ",t.message.toString())
                    //result.postValue(false)
                }
            });
        } catch(e:Exception){

            Log.e("excccccc",e.message.toString())

        }



        /*
        try {
            val firebase=FirebaseDatabase.getInstance().getReference("All Users");

            firebase.child(myKey).child("Requests").child(myKey + hisKey).child("state")
                .setValue("AcceptedByMe");
            firebase.child(hisKey).child("Requests").child(hisKey + myKey).child("state")
                .setValue("AcceptedByHim");
            viewModelScope.launch(Dispatchers.IO) {

                val books = async(Dispatchers.IO) {
                    getOurBooks(myKey, hisKey);
                }

                launch {
                    removeBooks(myKey, books.await());
                }

                launch {
                    removeBooks(hisKey, books.await());
                }


            }

        }catch (e:Exception){

            AppUtils.LOG("accept the request Exception ${e.message.toString()} ")
        }


         */
    }

    private fun removeBookFromAllBooks(key:String) {
        val firebaseDatabase=FirebaseDatabase.getInstance()
        firebaseDatabase.getReference("AllBooks").child(key).removeValue().addOnCompleteListener {
            if(it.isSuccessful){


            }else{
                AppUtils.LOG("removing from all books not successful")
            }
        }


    }

    suspend  fun removeBooks(key: String,books:ArrayList<Book>){

        for(i in books)
            Log.i("see books","books are ${i.title}");

        AppUtils.LOG("remove books is called ${books.size}")

        val firebase=FirebaseDatabase.getInstance().getReference("All Users")
        val bookks=firebase.child(key).child("Books").get().await()
        AppUtils.LOG("bookks size is $bookks and the uid is $key")
        for (i in bookks.children){
            val book=i.getValue(Book::class.java)
            AppUtils.LOG("book is  ${book!!.title}")
            if(contain(books,book)){
                AppUtils.LOG("yes it is in ")
                i.ref.removeValue()
                //removeBookFromAllBooks(book.key.toString())

            }
        }

        val requests=firebase.child(key).child("Requests").get().await()
        /*
        for (i in requests.children){
            val request=i.getValue(Request::class.java)
            if(request?.state=="AcceptedByHim"||request?.state=="AcceptedByMe")
                continue

            for (book in request?.myBooks!!) {

                if (contain(books,book)) {

                    i.ref.child("state").setValue("RefusedByMe")
                  //  cancelRequestsEmbedded(request.hisKey.toString(),request.myKey.toString())

                }
            }
        }

         */



    }


    private fun cancelRequestsEmbedded(hiskey: String, myKey: String){
        val firebase=FirebaseDatabase.getInstance().getReference("All Users")

        try {


            viewModelScope.launch(Dispatchers.IO) {
                firebase.child(hiskey).child("Requests").child(hiskey + myKey).child("state").setValue("RefusedByHim")
                    .await()
            }
        }catch (_: Exception){


        }
    }
    suspend fun getOurBooks(myKey: String,hisKey: String):ArrayList<Book>{



        val books=ArrayList<Book>()
        val firebaseDatabase = FirebaseDatabase.getInstance().reference



        val myBooksr=firebaseDatabase.child("All Users").child(myKey).child("Requests").child(myKey + hisKey).child("myBooks").get().await()
        val hisBooksr=firebaseDatabase.child("All Users").child(myKey).child("Requests").child(myKey + hisKey).child("hisBooks").get().await()

        for(i in myBooksr.children){

            val book=i.getValue(Book::class.java)
            AppUtils.LOG("Booksr  = $i")
            if (book != null) {
                AppUtils.LOG("NOT NULL")
                books.add(book)
            }
        }
        for(i in hisBooksr.children){

            val book=i.getValue(Book::class.java)
            books.add(book!!)
        }


        AppUtils.LOG("we have got the books we have ${books.size}")
        return books
    }


    suspend fun removeToMe(myKey:String, hisKey:String): Boolean {



        var  check=true
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference


            try {
                val r=firebaseDatabase.child("All Users").child(myKey).child("Requests").child(myKey + hisKey).get().await()

                val result=r.getValue(Request::class.java)
                //result?.seen=false
                result?.rstate="RefusedByMe"

                r.ref.setValue(result)


            } catch (e: Exception) {

                check=false
            }

        }
        job.join()
        return check
    }

    suspend fun removeToHim(myKey:String, hisKey:String): Boolean {

        var  check=true
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference



            try {
                val r=firebaseDatabase.child("All Users").child(hisKey).child("Books").child(  hisKey+myKey).get().await()

                val result=r.getValue(Request::class.java)
                //result?.seen=false
                result?.rstate="RefusedByHim"

                r.ref.setValue(result)
            } catch (e: Exception) {

                check=false;
            }

        }
        job.join()
        return check

    }


}