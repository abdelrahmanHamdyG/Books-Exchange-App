package com.example.bookexchange.UI

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.GridMyBooksAdapter
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.MyBooksViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBooksFragment(val context2:Context) : Fragment() {

    lateinit var myBooksList:ArrayList<Book>

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var grid:RecyclerView
    private lateinit var myBooksViewModel: MyBooksViewModel
    lateinit var dialogg:AlertDialog
    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth= FirebaseAuth.getInstance()
        myBooksViewModel= ViewModelProvider(this)[MyBooksViewModel::class.java];

        val view= inflater.inflate(R.layout.fragment_my_books, container, false)
        val floating=view.findViewById<FloatingActionButton>(R.id.my_books_floating)
        grid=view.findViewById(R.id.my_books_grid)

        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
        dialogg = AlertDialog.Builder(context2)
            .setView(dialogView)
            .setCancelable(false).create()
        dialogg.show()




        floating.setOnClickListener {

            Intent(activity, AddBookActivity::class.java).apply {
                startActivity(this)
            }


        }

        return view;
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()


        myBooksViewModel.myBooksList.observe(this) {

            dialogg.dismiss()
            if(it.size!=0) {


                grid.adapter = GridMyBooksAdapter(
                    myBooksViewModel.myBooksList.value!!,
                    firebaseAuth.currentUser!!.uid,
                    context2)

                grid.layoutManager = GridLayoutManager(activity, 2)

            }


        }

        GlobalScope.launch {
            myBooksViewModel.readTexts(firebaseAuth.currentUser!!.uid)
        }


    }


}