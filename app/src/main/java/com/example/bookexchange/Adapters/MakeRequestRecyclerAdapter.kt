package com.example.bookexchange.Adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.SendFromDialogToFragmentModel
import com.example.bookexchange.R

class MakeRequestRecyclerAdapter(var books:ArrayList<Book>,var imagesArr:ArrayList<SendFromDialogToFragmentModel>):RecyclerView.Adapter<MakeRequestRecyclerAdapter.viewHolder>() {


    inner class viewHolder(view: View):ViewHolder(view){
        val bookName=view.findViewById<TextView>(R.id.make_request_recycler_book)
        val bookDetails=view.findViewById<TextView>(R.id.make_request_recycler_details)
        val bookCategory=view.findViewById<TextView>(R.id.make_request_recycler_category)
        val bookImage=view.findViewById<ImageView>(R.id.make_request_recycler_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.make_request_recycler, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {


        holder.bookName.text="Book Name: ${books[position].bookName}"
        holder.bookDetails.text="Book Details: ${books[position].bookDescription}"
        holder.bookCategory.text="Book Category: ${books[position].category}"


        val bookBitMap=imagesArr[position].byteArray

        AppUtils.LOG("the BookName  is ${books[position].bookName}")

        val imageBitmap= BitmapFactory.decodeByteArray(bookBitMap,0,bookBitMap!!.size)
        holder.bookImage.setImageBitmap(imageBitmap)



    }
}