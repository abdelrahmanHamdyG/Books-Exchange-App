package com.example.bookexchange.UI

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.GridHomeAdapter
import com.example.bookexchange.Adapters.OnItemClickListener
import com.example.bookexchange.Adapters.SimpleHorizontalRecycler
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.Searchable
import com.example.bookexchange.ViewModels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class HomeFragment(val context2: Context) : Fragment(),OnItemClickListener,Searchable {

    lateinit var recycler: RecyclerView
    lateinit var homeFragmentViewModel:HomeFragmentViewModel
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var dialogg: AlertDialog
    lateinit var job1: Job;
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
            homeFragmentViewModel.readAllBooks(firebaseAuth.currentUser!!.uid,1,"");

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
        Log.e("mask 7 is ", mask[7]!!.toString())
        if(mask[7]==0) {

            GlobalScope.launch(Dispatchers.Default) {

                homeFragmentViewModel.filterByCategory(mask)

            }
        }else{

            GlobalScope.launch(Dispatchers.Default) {
                val uid=FirebaseAuth.getInstance().currentUser!!.uid.toString()

                homeFragmentViewModel.filterByFavourite(uid)

            }


        }


    }

    override fun onStop() {
        super.onStop()
        job1.cancel()
    }

    override fun onSearchQuery(query: String) {
        if(query=="none"){
            job1 = GlobalScope.launch(Dispatchers.IO) {
                homeFragmentViewModel.readAllBooks(firebaseAuth.currentUser!!.uid, 1, query);

            }
        }else {
            job1 = GlobalScope.launch(Dispatchers.IO) {
                homeFragmentViewModel.readAllBooks(firebaseAuth.currentUser!!.uid, 2, query);

            }
        }


    }


}