package com.example.bookexchange.UI

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.bookexchange.Adapters.MakeRequestRecyclerAdapter
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.SendFromDialogToFragmentModel
import com.example.bookexchange.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth



class MakeRequestActivity : AppCompatActivity(),FinishingFragmentListener {
    lateinit var myBooks:RecyclerView
    lateinit var hisBooks: RecyclerView
    lateinit var dialog: ChoosingBooksDialog
    lateinit var firebaseAuth:FirebaseAuth
    lateinit var makeRequestButton:AppCompatButton
    var myChoosenBooks=ArrayList<Book>()
    var his=true;
    var my=false;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_request)

        firebaseAuth=FirebaseAuth.getInstance()


        val bookKey=intent.getStringExtra("book_key")
        val bookName= intent.getStringExtra("book_name").toString()
        val bookImage=intent.getStringExtra("image_uri")
        val bookBitMap=intent.getByteArrayExtra("image_bitmap")
        val bookDetails= intent.getStringExtra("book_details")!!
        val bookCategory= intent.getStringExtra("book_category")
        val userKey=intent.getStringExtra("user")
        val city=intent.getStringExtra("user")
        val imageBitmap= BitmapFactory.decodeByteArray(bookBitMap,0,bookBitMap!!.size)
        val book=Book(bookName,bookDetails,bookCategory!!,bookImage!!,userKey!!,bookKey!!,city!!)
        makeRequestButton=findViewById<AppCompatButton>(R.id.make_request_button)


        val tempList=ArrayList<Book>()
        tempList.add(book)
        val tempImagesArr=ArrayList<SendFromDialogToFragmentModel>()
        tempImagesArr.add(SendFromDialogToFragmentModel(0,bookBitMap))

        hisBooks=findViewById(R.id.make_request_your_recycler)
        myBooks=findViewById(R.id.make_request_my_recycler)
        val addMy=findViewById<FloatingActionButton>(R.id.make_request_my_floating)
        val addYour=findViewById<FloatingActionButton>(R.id.make_request_your_floating)



        hisBooks.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        myBooks.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        hisBooks.adapter=MakeRequestRecyclerAdapter(tempList,tempImagesArr)
        addMy.setOnClickListener {
            val auth=firebaseAuth.currentUser!!.uid
            dialog = ChoosingBooksDialog(auth)
            dialog.setListener(this)
            dialog.show(supportFragmentManager, "CustomDialog")
        }

        addYour.setOnClickListener {
            dialog = ChoosingBooksDialog(userKey)
            dialog.setListener(this)
            dialog.show(supportFragmentManager, "CustomDialog2")
        }
        


    }

    override fun setListener(
        arr: ArrayList<Book>,
        arr2: ArrayList<SendFromDialogToFragmentModel>,
        uid: String
    ) {


        AppUtils.LOG("listener checked")
        dialog.dismiss()
        val adapter= MakeRequestRecyclerAdapter(arr,arr2)

        if(uid==firebaseAuth.currentUser!!.uid) {

            my=(arr.size!=0);
            myBooks.adapter = adapter

        }else{
            his=(arr.size!=0);
            hisBooks.adapter = adapter
        }

        makeRequestButton.isEnabled=my&&his;

    }


}