package com.example.bookexchange.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import java.util.zip.Inflater

class MakeRequestRecyclerAdapter(var books:ArrayList<Book>):RecyclerView.Adapter<MakeRequestRecyclerAdapter.viewHolder>() {


    inner class viewHolder(view: View):ViewHolder(view){
        val bookName=view.findViewById<TextView>(R.id.make_request_recycler_book)
        val bookDetails=view.findViewById<TextView>(R.id.make_request_recycler_book)
        val bookCategory=view.findViewById<TextView>(R.id.make_request_recycler_book)
        val bookImage=view.findViewById<TextView>(R.id.make_request_recycler_book)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

    }
}