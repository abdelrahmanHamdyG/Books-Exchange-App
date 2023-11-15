package com.example.bookexchange.UI

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.GridHomeAdapter
import com.example.bookexchange.Adapters.OnItemClickListener
import com.example.bookexchange.Adapters.SimpleHorizontalRecycler
import com.example.bookexchange.AppUtils
import com.example.bookexchange.AppUtils.texts
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragment(val context2: Context) : Fragment(),OnItemClickListener {

    lateinit var recycler: RecyclerView
    lateinit var homeFragmentViewModel:HomeFragmentViewModel
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var dialogg: AlertDialog
    lateinit var job1: Job;
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val dialogView = layoutInflater.inflate(R.layout.progress_dialog, null)
         dialogg = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false).create()



        val view= inflater.inflate(R.layout.fragment_home, container, false)

        recycler=view.findViewById<RecyclerView>(R.id.fragment_home_recycler)
        val horizontalRecycler=view.findViewById<RecyclerView>(R.id.fragment_home_horizontal_recycler)


        homeFragmentViewModel=ViewModelProvider(this)[HomeFragmentViewModel::class.java]

        recycler.layoutManager=GridLayoutManager(activity, 2)

        firebaseAuth=FirebaseAuth.getInstance()


        job1=GlobalScope.launch(Dispatchers.IO) {
            homeFragmentViewModel.readAllBooks(firebaseAuth.currentUser!!.uid);

        }

        homeFragmentViewModel.filteredBooksList.observe(requireActivity()){

            dialogg.dismiss()

            if(it.size==0){

                recycler.visibility=View.GONE


            }else {

                recycler.visibility=View.VISIBLE


                val adapter = GridHomeAdapter(it, context2)

                recycler.adapter = adapter

            }

        }




        val horizontalAdapter=SimpleHorizontalRecycler()
        horizontalAdapter.setItemClickListener(this);
        horizontalRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        horizontalRecycler.adapter =horizontalAdapter




        var firebaseDatabase= FirebaseDatabase.getInstance().reference
        //firebaseDatabase.child("AllBooks").addValueEventListener(valueEventListener())


        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onItemClick(mask: Array<Int>) {


        dialogg.show()

        GlobalScope.launch (Dispatchers.Default){

            homeFragmentViewModel.filterByCategory(mask)

        }


    }

    override fun onStop() {
        super.onStop()
        job1.cancel()
    }


}