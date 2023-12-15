package com.example.bookexchange.ViewModels


import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.bookexchange.Models.Request
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.SendFromDialogToFragmentModel
import com.example.bookexchange.UI.RequestsFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Types.NULL
import java.util.TreeSet


fun contain(books:ArrayList<Book>, book:Book):Boolean{

    for(i in books) {
        if (i.bookName==book.bookName &&i.bookDescription==book.bookDescription)
            return true;
    }
    return false;
}
class MakingDecisionViewModel: ViewModel() {


    var request= MutableLiveData<Request>()
    var numberOfReadRequest=0;

    var result= MutableLiveData<Boolean>()



    suspend fun readTheRequest(uid:String, key:String) {

        val firebaseDatabase=FirebaseDatabase.getInstance().reference


        var  _request:Request?= Request("Null","Null",false,false,"Null",false,0);

        var found=false;



                val response =
                    firebaseDatabase.child("All Users").child(uid).child("Requests").child(key)
                        .get()
                        .await()

                AppUtils.LOG("MakingDecisionViewModel:readTheRequest:Job:reading response response is:${response.toString()}")

        _request = response.getValue(Request::class.java)!!
                found=true;

        AppUtils.LOG("MakingDecisionViewModel:readTheRequest:Job after join")
        request.postValue(_request)

    }

    suspend fun readTheBooksImages(arr:ArrayList<Book>):ArrayList<SendFromDialogToFragmentModel >{


        val firebaseStorage=FirebaseStorage.getInstance().reference

        val books=ArrayList<SendFromDialogToFragmentModel>()

        var count=0;

        for( i in arr) {

            AppUtils.LOG(" the image is number $count")
            try {

                AppUtils.LOG(" the image uri is ${i.imageUri}")
                val imageAsByte=firebaseStorage.child(i.imageUri.toString()).getBytes(4096 * 4096).await()

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


    fun cancelRequest(myKey:String,hisKey: String){

        viewModelScope.launch(Dispatchers.IO){

            val first=async { removeToMe(myKey,hisKey) }
            val second=async { removeToHim(myKey,hisKey)  }

            if(first.await()&&second.await()){

                result.postValue(true);
            }else{

                result.postValue(false);
            }
        }
    }



    @SuppressLint("SuspiciousIndentation")
    fun acceptTheRequest(myKey: String, hisKey: String){


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

    }

    suspend  fun removeBooks(key: String,books:ArrayList<Book>){

        for(i in books)
            Log.i("see books","books are ${i.bookName}");

        AppUtils.LOG("remove books is called ${books.size}")

        val firebase=FirebaseDatabase.getInstance().getReference("All Users");
        val bookks=firebase.child(key).child("Books").get().await()
        AppUtils.LOG("bookks size is $bookks and the uid is $key");
        for (i in bookks.children){
            val book=i.getValue(Book::class.java)
            AppUtils.LOG("book is  ${book!!.bookName}");
            if(contain(books,book)){
            AppUtils.LOG("yes it is in ")
                i.ref.removeValue()

            }
        }

        val requests=firebase.child(key).child("Requests").get().await()
        for (i in requests.children){
            val request=i.getValue(Request::class.java)
            if(request?.state=="AcceptedByHim"||request?.state=="AcceptedByMe")
                continue;

            for (book in request?.myBooks!!) {

                if (contain(books,book)) {

                    i.ref.child("state").setValue("RefusedByMe")
                    cancelRequestsEmbedded(request.hisKey.toString(),request.myKey.toString())

                }
            }
        }



    }


    private fun cancelRequestsEmbedded(hiskey: String, myKey: String){
        val firebase=FirebaseDatabase.getInstance().getReference("All Users")

        try {


            viewModelScope.launch(Dispatchers.IO) {
                firebase.child(hiskey).child("Requests").child(hiskey + myKey).child("state").setValue("RefusedByHim")
                    .await();
            }
        }catch (e: Exception){


        }
    }
    suspend fun getOurBooks(myKey: String,hisKey: String):ArrayList<Book>{



            val books=ArrayList<Book>();
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
        return books;
    }


    suspend fun removeToMe(myKey:String, hisKey:String): Boolean {



        var  check=true;
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference


            try {
                val r=firebaseDatabase.child("All Users").child(myKey).child("Requests").child(myKey + hisKey).get().await()

                val result=r.getValue(Request::class.java)
                result?.seen=false;
                result?.state="RefusedByMe"

                r.ref.setValue(result)


            } catch (e: Exception) {

                check=false;
            }

        }
        job.join()
        return check
    }

    suspend fun removeToHim(myKey:String, hisKey:String): Boolean {

        var  check=true;
        val job=viewModelScope.launch(Dispatchers.IO) {
            val firebaseDatabase = FirebaseDatabase.getInstance().reference



            try {
                val r=firebaseDatabase.child("All Users").child(hisKey).child("Books").child(  hisKey+myKey).get().await()

                val result=r.getValue(Request::class.java)
                result?.seen=false;
                result?.state="RefusedByHim"

                r.ref.setValue(result)
            } catch (e: Exception) {

                check=false;
            }

        }
        job.join()
        return check

    }


}