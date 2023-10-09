package com.example.bookexchange.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.RequestsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RequestsFragment(val lastCount: Int) : Fragment() {

    lateinit var requestsFragmentViewModel:RequestsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_requests, container, false)
        val firebaseAuth=FirebaseAuth.getInstance()

        requestsFragmentViewModel=ViewModelProvider(this)[RequestsFragmentViewModel::class.java]


        GlobalScope.launch(Dispatchers.IO){


            requestsFragmentViewModel.makeEveryThingSeen(firebaseAuth.currentUser!!.uid)

        }

        if(lastCount>0){

        }


        return view;

    }


}