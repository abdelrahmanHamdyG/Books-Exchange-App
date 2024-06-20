package com.example.bookexchange.UI

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.MakeRequestRecyclerAdapter
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.MakingDecisionViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.sql.Types.NULL

class MakingDecisionActivity : AppCompatActivity() {



    lateinit var dialogg: androidx.appcompat.app.AlertDialog

    lateinit var makingDecisionViewModel: MakingDecisionViewModel
    lateinit var firebaseAuth:FirebaseAuth;
    lateinit var requestName:String
    lateinit var job: Job
     var rid:Int?=NULL
    var dismissed=false;
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_decision)

        val myKey=intent.extras!!.getString("myKey")//uid1
        val hisKey=intent.extras!!.getString("hisKey")// uid2
        val fromMe=intent.extras!!.getBoolean("fromMe")
        rid= intent.extras!!.getInt("rid");

        requestName=myKey+hisKey

        val positiveButton=findViewById<AppCompatButton>(R.id.make_decision_positive_button)
        firebaseAuth=FirebaseAuth.getInstance()
        val negativeButton=findViewById<AppCompatButton>(R.id.make_decision_negative_button)
        val nBooksText=findViewById<TextView>(R.id.make_decision_nbooks)
        val state=findViewById<TextView>(R.id.make_decision_state)
        val myRecycler=findViewById<RecyclerView>(R.id.make_decision_my_recycler)
        val yourRecycler=findViewById<RecyclerView>(R.id.make_decision_your_recycler)



        myRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        yourRecycler.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        makingDecisionViewModel=ViewModelProvider(this)[MakingDecisionViewModel::class.java]



        makingDecisionViewModel.request.observe(this){

            AppUtils.LOG("Observer is triggered");


            val nBooksString=it.myBooks?.size.toString() + " Books vs "+it.hisBooks!!.size.toString()+ " Books";
            nBooksText.text= nBooksString

            if(it.state=="pending"&&fromMe){



                state.text="Waiting Response"
                positiveButton.visibility=View.GONE
                negativeButton.visibility=View.GONE
            }else {

                if (it.state == "pending" && !fromMe) {


                    state.text="response please"
                    positiveButton.visibility=View.VISIBLE
                    negativeButton.visibility=View.VISIBLE

                } else {


                     if (it.state == "refused") {
                        state.text = "Cancelled"
                        negativeButton.visibility = View.GONE
                        positiveButton.visibility = View.GONE
                        //positiveButton.text = "See contact";
                    } else {
                        if (it.state == "accepted" ) {
                            state.text = "The request is Completed ^_^"
                            negativeButton.visibility = View.GONE
                            positiveButton.visibility = View.VISIBLE
                            positiveButton.text = "See contact";
                        }


                    }


                }

            }

            makingDecisionViewModel.viewModelScope.launch(Dispatchers.IO) {
                val job1 = makingDecisionViewModel.viewModelScope.async {

                    makingDecisionViewModel.readTheBooksImages(it.myBooks!!)

                }
                val job2 = makingDecisionViewModel.viewModelScope.async {

                        makingDecisionViewModel.readTheBooksImages(it.hisBooks!!)
                }



                withContext(Dispatchers.Main){

                    try {
                        myRecycler.adapter=MakeRequestRecyclerAdapter(this@MakingDecisionActivity,it.myBooks!!,job1.await())

                        yourRecycler.adapter =
                           MakeRequestRecyclerAdapter(this@MakingDecisionActivity,it.hisBooks!!, job2.await())
                    }catch (e:Exception){
                        AppUtils.LOG("trying second recycler " +e.message.toString())
                    }


                }

            }

        }







        makingDecisionViewModel.result.observe(this){

            dialogg.dismiss()

            dismissed=true;
            if(it) {
                AppUtils.showToast(this, "the request is cancelled successfully")
                finish()
            }
            else
                AppUtils.showToast(this,"Please check your internet connection and try again")


        }







        negativeButton.setOnClickListener {



            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Confirm deletion?")
            alertDialog.setMessage("Are you sure you want to cancel this request?")
            alertDialog.setPositiveButton("Yes") { dialog, _ ->

                val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                dialogg = androidx.appcompat.app.AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setCancelable(false).create()
                dialogg.show()
                val handler = Handler(Looper.getMainLooper())
                val delayMillis = 8000L

                handler.postDelayed({
                    dismissProgressDialog()
                }, delayMillis)

                makingDecisionViewModel.cancelRequest(rid!!)

            }
            alertDialog.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }


            val x=alertDialog.create()
            x.show()



        }


        positiveButton.setOnClickListener {

            if (positiveButton.text == "See contact") {

            val toContactActivity=Intent(this@MakingDecisionActivity,InformationOfContactActivity::class.java)

                if(fromMe) {
                    toContactActivity.putExtra("hisKey", hisKey)
                    toContactActivity.putExtra("myKey", myKey)
                }else{
                    toContactActivity.putExtra("hisKey",myKey)
                    toContactActivity.putExtra("myKey", hisKey)

                }

            startActivity(toContactActivity)

            } else {


                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Confirm request?")
                alertDialog.setMessage("Are you sure you want to Confirm this request?")
                alertDialog.setPositiveButton("Yes") { dialog, _ ->

                    val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
                    dialogg = androidx.appcompat.app.AlertDialog.Builder(this)
                        .setView(dialogView)
                        .setCancelable(false).create()
                    dialogg.show()



                    Log.i("accepting the request","yes")
                    makingDecisionViewModel.acceptTheRequest(myKey.toString(), rid!!)
                    dialogg.dismiss()
                    val intent=Intent(this,InformationOfContactActivity::class.java)
                    if(fromMe) {
                        intent.putExtra("hisKey", hisKey)
                        intent.putExtra("myKey", myKey)
                    }else{
                        intent.putExtra("hisKey",myKey)
                        intent.putExtra("myKey", hisKey)

                    }

                    startActivity(intent)


                }
                alertDialog.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }


                val x = alertDialog.create()
                x.show()


            }
        }
    }

    fun dismissProgressDialog(){

        // when I reconnect the internet it cancel the request
        if(!dismissed)
            dialogg.dismiss()

    }

    override fun onStart() {
        super.onStart()


        job=makingDecisionViewModel.viewModelScope.launch(Dispatchers.IO) {
            AppUtils.LOG("MakingDecisionActivity: readTheRequests")
            makingDecisionViewModel.readTheRequest(firebaseAuth.currentUser!!.uid, rid!!);
        }

    }

}