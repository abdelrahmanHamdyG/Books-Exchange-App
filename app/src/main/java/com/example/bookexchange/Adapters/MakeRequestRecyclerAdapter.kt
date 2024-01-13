package com.example.bookexchange.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.SendFromDialogToFragmentModel
import com.example.bookexchange.R
import com.example.bookexchange.UI.BooksDetailsActivity
import java.io.ByteArrayOutputStream

class MakeRequestRecyclerAdapter(val context:Context,var books:ArrayList<Book>,var imagesArr:ArrayList<SendFromDialogToFragmentModel>):RecyclerView.Adapter<MakeRequestRecyclerAdapter.viewHolder>() {


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


        holder.itemView.setOnClickListener {

            try {


                val imageBitmap = (holder.bookImage.drawable as? BitmapDrawable)?.bitmap
                val stream = ByteArrayOutputStream()
                imageBitmap?.compress(Bitmap.CompressFormat.PNG, 60, stream)
                val byteArray = stream.toByteArray()


                val i = Intent(context, BooksDetailsActivity::class.java)
                i.putExtra("editable", false);
                i.putExtra("book_name", books[position].bookName)
                i.putExtra("book_details", books[position].bookDescription)
                i.putExtra("image_uri", books[position].imageUri)
                i.putExtra("image_bitmap", byteArray);
                i.putExtra("book_key", books[position].key)
                i.putExtra("book_category", books[position].category)
                i.putExtra("city", books[position].city)

                context.startActivity(i);

            }catch (e:Exception){
                AppUtils.LOG("exception is ${e.message}")
            }
        }



    }
}