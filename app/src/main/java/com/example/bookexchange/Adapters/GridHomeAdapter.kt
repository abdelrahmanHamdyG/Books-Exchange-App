package com.example.bookexchange.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.google.firebase.storage.FirebaseStorage


class GridHomeAdapter(var arrayList:ArrayList<Book>): RecyclerView.Adapter<GridHomeAdapter.viewholder>() {

    inner class viewholder(view: View): RecyclerView.ViewHolder(view){

        var image=view.findViewById<ImageView>(R.id.home_grid_image)
        var bookName=view.findViewById<TextView>(R.id.home_grid_book_name)
        var bookDetails=view.findViewById<TextView>(R.id.home_grid_details)
        var button=view.findViewById<AppCompatButton>(R.id.home_grid_exchange)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_grid, parent, false)

        return viewholder(view)



    }

    override fun getItemCount(): Int {

        return arrayList.size

    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {

        val storageReference = FirebaseStorage.getInstance().reference



        val imageRef = storageReference.child(arrayList[position].imageUri.toString())
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(holder.itemView.context)
                    .load(uri)
                    .into(holder.image)
            }
            .addOnFailureListener { exception ->
                // Handle any errors that occur while fetching the image
                Log.i("my_trag", "Error loading image: ${exception.message}")
            }

        holder.bookName.text=arrayList[position].bookName
        holder.bookDetails.text=arrayList[position].bookDescription


    }

}