package com.example.bookexchange.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.RequestsRecyclerAdapter
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.RequestsFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RequestsFragment() : Fragment() {

    lateinit var requestsFragmentViewModel:RequestsFragmentViewModel
    lateinit var recycler:RecyclerView
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        AppUtils.LOG("RequestsFragment:OnCreate()")
        val view= inflater.inflate(R.layout.fragment_requests, container, false)

        recycler=view.findViewById<RecyclerView>(R.id.requests_fragment_recycler)







        return view;

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth=FirebaseAuth.getInstance()

        requestsFragmentViewModel=ViewModelProvider(this)[RequestsFragmentViewModel::class.java]


        requestsFragmentViewModel.makeEveryThingSeen(firebaseAuth.currentUser!!.uid)




        recycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)


        requestsFragmentViewModel.requests.observe(viewLifecycleOwner){

            AppUtils.LOG("triggered requests ${it.size}")

            recycler.adapter=RequestsRecyclerAdapter(it,requireActivity());

        }
    }

    override fun onStart() {
        super.onStart()
        requestsFragmentViewModel.readRequests(firebaseAuth.currentUser!!.uid)

    }



}