package com.example.bookexchange.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.UI.BooksDetailsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GridMyBooksAdapter(var booksList:ArrayList<Book>, var uid:String, var context: Context):RecyclerView.Adapter<GridMyBooksAdapter.viewHolder>() {

    inner class viewHolder(view : View):RecyclerView.ViewHolder(view){

        var bookImage=view.findViewById<ImageView>(R.id.add_books_grid_image)
        var textView=view.findViewById<TextView>(R.id.add_books_grid_text)
        var deleteButton=view.findViewById<ImageView>(R.id.add_books_grid_delete)
        var progress=view.findViewById<ProgressBar>(R.id.add_books_grid_progress)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_books_grid, parent, false)

        return viewHolder(view)

    }

    override fun getItemCount(): Int {

        return booksList.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Log.i("my_trag","I am in the adapter")


        val storageReference = FirebaseStorage.getInstance().getReference(booksList[position].imageUri.toString()).downloadUrl.addOnCompleteListener{

            if(it.isSuccessful) {
                Glide.with(context)
                    .load(it.result.toString())
                    .into(holder.bookImage)
            }else{
                Log.i("my_trag",it.exception?.message.toString())

            }

        }


        holder.textView.text=booksList[position].bookName

        holder.deleteButton.setOnClickListener {

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Confirm deletion?")
            alertDialog.setMessage("Are you sure you want to delete this item?")
            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                var k=booksList[position].key.toString()
                GlobalScope.launch(Dispatchers.IO){

                    var returned=removeValue(k)
                    withContext(Dispatchers.Main){

                        dialog.dismiss()
                        if(returned) {
                            booksList.removeAt(position)
                            notifyDataSetChanged()
                        }
                    }
                }

            }
            alertDialog.setNegativeButton("No") { dialog, _ ->
             dialog.dismiss()
            }


            var x=alertDialog.create()
            x.show()

        }



        holder.bookImage.setOnClickListener {



            var intent= Intent(context, BooksDetailsActivity::class.java)
            Log.i("my_trag", booksList[position].city!!+" hhhhh")
            intent.putExtra("book_name",booksList[position].bookName)
            intent.putExtra("book_details",booksList[position].bookDescription)
            intent.putExtra("book_image",booksList[position].imageUri)
            intent.putExtra("book_key",booksList[position].key)
            intent.putExtra("book_category",booksList[position].category)
            context.startActivity( intent)

        }

    }



    suspend fun removeValue(key:String):Boolean{

        val firebaseDataBase = FirebaseDatabase.getInstance().getReference()
        var theReturn=true;

        val job1=GlobalScope.launch(Dispatchers.IO) {

        try {


            firebaseDataBase.child("AllBooks").child(key).removeValue().await()
        }catch (e:Exception){

            theReturn=false;
        }

        }

        val job2=GlobalScope.launch(Dispatchers.IO) {
            try {


                val response =
                    firebaseDataBase.child("All Users").child(uid).child("Books").get().await()
                for (i in response.children) {
                    val book = i.getValue(Book::class.java)
                    if (book!!.key == key) {
                        firebaseDataBase.child("All Users").child(uid).child("Books").child(i.key!!)
                            .removeValue().await()
                    }

                }
            }catch (e:Exception){
                theReturn=false;

            }
        }
        job1.join()
        job2.join()
        return theReturn;

     }




}