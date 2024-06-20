package com.example.bookexchange.UI

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Favourites
import com.example.bookexchange.Models.UserData
import com.example.bookexchange.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class InformationOfContactActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information_of_contact)


        val textPhone=findViewById<TextView>(R.id.textViewPhone)
        val textAddress=findViewById<TextView>(R.id.textViewAddress)
        val textName=findViewById<TextView>(R.id.textViewName)
        val favouriteImage=findViewById<ImageButton>(R.id.favourite_image)
        val favouriteImage2=findViewById<ImageButton>(R.id.favourite_image2)
        val hisKey=intent.extras?.getString("hisKey");
        val myKey=intent.extras?.getString("myKey");


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


        favouriteImage.setOnClickListener{
            favouriteImage.visibility = View.GONE
            favouriteImage2.visibility=View.VISIBLE

            api.addFavourite(myKey!!,hisKey!!).enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("rs",response.message().toString())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("ga",t.message.toString())
                }


            })
        }

        favouriteImage2.setOnClickListener{
            favouriteImage.visibility = View.VISIBLE
            favouriteImage2.visibility=View.GONE

            api.removeFavourite(myKey!!,hisKey!!).enqueue(object :Callback<String>{
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.e("rs",response.message().toString())
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("ga",t.message.toString())
                }


            })


        }

        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        val dialogg = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false).create()


        AppUtils.LOG("To Information Activity")

        dialogg.show()



        val books=api.getAClient(hisKey!!)

        books.enqueue(object :Callback<List<UserData>>{

            override fun onResponse(call: Call<List<UserData>>, response: Response<List<UserData>>) {

                Log.i("response ",response.message().toString())
                val userData= response.body()!![0]
                val fname = userData?.fname
                val lname=userData?.lname
                val governorate = userData?.governorate
                val detailed = userData?.detailed_address
                val phone=userData?.phone_num

                textAddress.text="Address: $governorate, $detailed"
                textPhone.text="phone: $phone"
                textName.text="name : $fname $lname";

                Log.e("the my key is ",myKey!!)
                val fav=Favourites(myKey!!,hisKey)
                api.checkFavourite(myKey!!,hisKey).enqueue(object: Callback<List<Favourites>>{
                    override fun onResponse(
                        call: Call<List<Favourites>>,
                        response: Response<List<Favourites>>
                    ) {
                        Log.e("errrrrrrra",response.message())
                        if (response.isSuccessful) {
                            Log.e("Error", "Response successful")
                            val isFavourite = response.body()

                            if (isFavourite!!.size==1) {
                                favouriteImage.visibility = View.GONE
                                favouriteImage2.visibility=View.VISIBLE
                            } else {
                                favouriteImage.visibility = View.VISIBLE
                                favouriteImage2.visibility=View.GONE
                            }
                        } else {
                            Log.e("Error", "Response not successful")
                            favouriteImage.visibility = View.GONE
                        }

                        dialogg.dismiss()
                    }

                    override fun onFailure(call: Call<List<Favourites>>, t: Throwable) {
                        Log.e("errrrrrrr",t.message.toString())

                        dialogg.dismiss()
                    }


                })


            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                dialogg.dismiss()
                Log.e("eRorr",t.message.toString())
            }
        })





        /*
        val databaseReference = FirebaseDatabase.getInstance().getReference("All Users").child(hisKey.toString()).child("DATA")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(UserData::class.java)

                    val fname = userData?.fname
                    val lname=userData?.lname
                    val governorate = userData?.governorate
                    val detailed = userData?.detailed_address
                    val phone=userData?.phone_num

                    textAddress.text="Address: $governorate, $detailed"
                    textPhone.text="phone: $phone"
                    textName.text="name : $fname";

                    AppUtils.LOG("text should change")

                    dialogg.dismiss()
                } else {

                    dialogg.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {

                AppUtils.LOG("text should change: cancelled")
                dialogg.dismiss()
            }
        })

    */
    }


}