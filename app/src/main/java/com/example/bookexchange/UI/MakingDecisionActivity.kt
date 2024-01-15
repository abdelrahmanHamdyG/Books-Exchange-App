package com.example.bookexchange.UI

import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.MakeRequestRecyclerAdapter
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Request
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.MakingDecisionViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*

class MakingDecisionActivity : AppCompatActivity() {



    lateinit var dialogg: androidx.appcompat.app.AlertDialog

    lateinit var makingDecisionViewModel: MakingDecisionViewModel
    lateinit var firebaseAuth:FirebaseAuth;
    lateinit var job: Job
    var dismissed=false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_decision)

        val myKey=intent.extras!!.getString("myKey")
        val hisKey=intent.extras!!.getString("hisKey")
        val fromMe=intent.extras!!.getBoolean("fromMe")
        val requestName=myKey+hisKey

        val positiveButton=findViewById<AppCompatButton>(R.id.make_decision_positive_button)
        val firebaseAuth=FirebaseAuth.getInstance()
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

            if(it.state=="Sent"){

                state.text="Waiting Response"
                positiveButton.visibility=View.GONE
            }else{

                if(it.state=="Received")
                    state.text="We Are Waiting You :)"
                else if(it.state=="AcceptedByMe"||it.state=="AcceptedByHim"){
                    state.text="Done successfully"
                    negativeButton.visibility=View.GONE
                    positiveButton.visibility=View.VISIBLE
                    positiveButton.text="See contact";
                }else{
                    if(it.state=="RefusedByMe"||it.state=="RefusedByHim"){
                        state.text="The request is cancelled :("
                        negativeButton.visibility=View.GONE
                        positiveButton.visibility=View.GONE
                    }


                }


            }


            GlobalScope.launch(Dispatchers.IO) {
                val job1 = GlobalScope.async {

                    makingDecisionViewModel.readTheBooksImages(it.myBooks!!)

                }
                val job2 = GlobalScope.async {

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

        job=GlobalScope.launch(Dispatchers.IO) {
            AppUtils.LOG("MakingDecisionActivity: readTheRequests")
            makingDecisionViewModel.readTheRequest(firebaseAuth.currentUser!!.uid, requestName);
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

                makingDecisionViewModel.cancelRequest(myKey.toString(),hisKey.toString())

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
            toContactActivity.putExtra("hisKey",hisKey)
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


                    makingDecisionViewModel.acceptTheRequest(myKey.toString(), hisKey.toString())
                    /*Intent(this,InformationOfContactActivity::class.java).apply {
                    intent!!.putExtra("hisKey",hisKey);
                    startActivity(this);
                    }*/
                    dialogg.dismiss()
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
        job.cancel()
    }

}