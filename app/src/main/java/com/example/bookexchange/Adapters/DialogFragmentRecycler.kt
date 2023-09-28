package com.example.bookexchange.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.google.firebase.storage.FirebaseStorage
import java.util.zip.Inflater

interface BooksListener{
    fun onItemClick(size:Int)

}

class DialogFragmentRecycler(var booksList:ArrayList<Book>, var uid:String, var context: Context): RecyclerView.Adapter<DialogFragmentRecycler.viewHolder>() {


    var chosenBooks=ArrayList<Int>()
    lateinit var bookListener:BooksListener
    inner class viewHolder(view:View):ViewHolder(view){

        var bookImage=view.findViewById<ImageView>(R.id.add_books_grid_image)
        var textView=view.findViewById<TextView>(R.id.add_books_grid_text)
        var deleteButton=view.findViewById<ImageView>(R.id.add_books_grid_delete)
        var progress=view.findViewById<ProgressBar>(R.id.add_books_grid_progress)
        val layout=view.findViewById<LinearLayout>(R.id.add_books_grid_layout)

    }
    fun setListener(booksListener: BooksListener){

        this.bookListener=booksListener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.add_books_grid,parent,false)
        return viewHolder(view);
    }

    override fun getItemCount(): Int {


        return booksList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.textView.text=booksList[position].bookName
        holder.deleteButton.setImageResource(R.drawable.baseline_done_outline_24)
        holder.deleteButton.visibility=View.INVISIBLE

        holder.progress.visibility=View.VISIBLE
        FirebaseStorage.getInstance().getReference(booksList[position].imageUri.toString()).downloadUrl.addOnCompleteListener{

            if(it.isSuccessful) {
                Glide.with(context)
                    .load(it.result.toString())
                    .into(holder.bookImage)
            }else{
                Log.i("my_trag",it.exception?.message.toString())

            }
            holder.progress.visibility=View.GONE

        }
        holder.itemView.setOnClickListener {
        AppUtils.LOG("clicked dialog")
            if(holder.deleteButton.visibility==View.INVISIBLE) {
                AppUtils.LOG("clicked dialog")
                holder.deleteButton.visibility = View.VISIBLE
                holder.layout.setBackgroundResource(R.drawable.item_is_choosen)
                chosenBooks.add(position)
            }else{

                holder.deleteButton.visibility = View.INVISIBLE
                holder.layout.setBackgroundResource(R.color.white)
                chosenBooks.remove(position)
            }
            bookListener.onItemClick(chosenBooks.size)
        }

    }
}