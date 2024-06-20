package com.example.bookexchange.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookexchange.ApiInterface
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.UI.BooksDetailsActivity
import com.example.bookexchange.ViewModels.MyBooksViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.GsonBuilder
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.internal.notify
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayOutputStream

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

        val myBooksViewModel=MyBooksViewModel()

        holder.progress.visibility=View.VISIBLE
        FirebaseStorage.getInstance().getReference(booksList[position].image_link.toString()).downloadUrl.addOnCompleteListener{

            if(it.isSuccessful) {
                Glide.with(context)
                    .load(it.result.toString())
                    .into(holder.bookImage)
            }else{
                Log.i("my_trag",it.exception?.message.toString())

            }
            holder.progress.visibility=View.GONE

        }


        holder.textView.text=booksList[position].title

        holder.deleteButton.setOnClickListener {

            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("Confirm deletion?")
            alertDialog.setMessage("Are you sure you want to delete this item?")
            alertDialog.setPositiveButton("Yes") { dialog, _ ->
                //var k=booksList[position].key.toString()
                /*
                GlobalScope.launch(Dispatchers.IO){

                    val returned=myBooksViewModel.removeValue(uid,booksList[position].image_link!!)
                    withContext(Dispatchers.Main){

                        dialog.dismiss()
                        if(returned) {
                            booksList.removeAt(position)
                            notifyDataSetChanged()
                        }
                    }
                }
                */

                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                val logging = HttpLoggingInterceptor()
                logging.setLevel(HttpLoggingInterceptor.Level.BODY)

                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor(logging)

                val retrofit= Retrofit.Builder().baseUrl("https://database-project-2.onrender.com/api/v1/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build()


                val api =retrofit.create(ApiInterface::class.java)
                val call=api.deleteBook(booksList[position].bid)

                call.enqueue(object:Callback<String>{
                    override fun onResponse(call: Call<String>, response: Response<String>) {

                        val firebaseStorage=FirebaseStorage.getInstance().reference

                        try {
                            firebaseStorage.child(booksList[position].image_link!!).delete()
                        }catch (e:Exception){

                        }
                        booksList.removeAt(position);
                        notifyDataSetChanged();
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {

                    }
                })





            }
            alertDialog.setNegativeButton("No") { dialog, _ ->
             dialog.dismiss()
            }


            val x=alertDialog.create()
            x.show()

        }



        holder.bookImage.setOnClickListener {



            val imageBitmap = (holder.bookImage.drawable as? BitmapDrawable)?.bitmap
            val stream = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val intent= Intent(context, BooksDetailsActivity::class.java)


            intent.putExtra("flag",true);
            intent.putExtra("book_name",booksList[position].title)
            intent.putExtra("book_details",booksList[position].description)
            intent.putExtra("image_uri",booksList[position].image_link)
            intent.putExtra("book_bid",booksList[position].bid)
            intent.putExtra("image_bitmap",byteArray);
            //intent.putExtra("book_key",booksList[position].key)
            intent.putExtra("book_category",booksList[position].category)
            //intent.putExtra("city",booksList[position].city)

            context.startActivity( intent)

        }

    }







}