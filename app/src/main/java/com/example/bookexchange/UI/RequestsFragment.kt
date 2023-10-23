package com.example.bookexchange.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.RequestsRecyclerAdapter
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
        val view= inflater.inflate(R.layout.fragment_requests, container, false)

        val recycler=view.findViewById<RecyclerView>(R.id.requests_fragment_recycler)





        val firebaseAuth=FirebaseAuth.getInstance()

        requestsFragmentViewModel=ViewModelProvider(this)[RequestsFragmentViewModel::class.java]


        GlobalScope.launch(Dispatchers.IO){


            launch {
                requestsFragmentViewModel.makeEveryThingSeen(firebaseAuth.currentUser!!.uid)
            }

            launch {


                requestsFragmentViewModel
            }

        }


        requestsFragmentViewModel.readRequests(firebaseAuth.currentUser!!.uid)

        recycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


        requestsFragmentViewModel.requests.observe(requireActivity()){

            recycler.adapter=RequestsRecyclerAdapter(it,requireContext());


        }

        return view;

    }


}