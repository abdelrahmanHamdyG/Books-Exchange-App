package com.example.bookexchange.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.UI.BooksDetailsActivity
import com.example.bookexchange.UI.MakeRequestActivity
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class GridHomeAdapter(var arrayList:ArrayList<Book>,var context: Context): RecyclerView.Adapter<GridHomeAdapter.viewholder>() {

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




        val imageRef = storageReference.child(arrayList[position].image_link.toString())
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


        holder.itemView.setOnClickListener {


            val imageBitmap = (holder.image.drawable as? BitmapDrawable)?.bitmap
            val stream = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 60, stream)
            val byteArray = stream.toByteArray()


            val i = Intent(context, BooksDetailsActivity::class.java)
            i.putExtra("editable", false);
            i.putExtra("book_name", arrayList[position].title)
            i.putExtra("book_details", arrayList[position].description)
            i.putExtra("image_uri", arrayList[position].image_link)
            i.putExtra("image_bitmap", byteArray);
            //i.putExtra("book_key", arrayList[position].key)
            i.putExtra("book_category", arrayList[position].category)
            //i.putExtra("city", arrayList[position].city)

            context.startActivity(i);



        }

        holder.bookName.text=arrayList[position].title
        holder.bookDetails.text=arrayList[position].description

        holder.button.setOnClickListener {




            val imageBitmap = (holder.image.drawable as? BitmapDrawable)?.bitmap
            val stream = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()



            val intent=Intent(context,MakeRequestActivity::class.java)
            intent.putExtra("book_name",arrayList[position].title)
            intent.putExtra("book_details",arrayList[position].description)
            intent.putExtra("image_uri",arrayList[position].image_link)
            intent.putExtra("image_bitmap",byteArray);
            //intent.putExtra("book_key",arrayList[position].key)
            intent.putExtra("book_category",arrayList[position].category)
            intent.putExtra("book_bid",arrayList[position].bid)
            //intent.putExtra("city",arrayList[position].city)
            intent.putExtra("user",arrayList[position].uid)
            intent.putExtra("state",arrayList[position].bstate)


            context.startActivity(intent)
        }

    }

}