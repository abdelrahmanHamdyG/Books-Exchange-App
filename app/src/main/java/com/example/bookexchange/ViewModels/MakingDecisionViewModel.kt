package com.example.bookexchange.ViewModels


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

class MakingDecisionViewModel: ViewModel() {


    var request= MutableLiveData<Request>()
    var numberOfReadRequest=0;

    var result= MutableLiveData<Boolean>()



    suspend fun readTheRequest(uid:String, key:String) {

        val firebaseDatabase=FirebaseDatabase.getInstance().reference


        var  _request:Request?= Request("Null","Null",false,false,"Null",false,0);

        var found=false;


        val job=viewModelScope.launch(Dispatchers.IO) {
            try {


                val response =
                    firebaseDatabase.child("All Users").child(uid).child("Requests").child(key)
                        .get()
                        .await()

                _request = response.getValue(Request::class.java)!!
                found=true;

            } catch (e: Exception) {

                AppUtils.LOG("Exception is Found")
            }

        }

        job.join()

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
                val r=firebaseDatabase.child("All Users").child(hisKey).child("Requests").child(  hisKey+myKey).get().await()

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