package com.example.bookexchange.UI

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.GridHomeAdapter
import com.example.bookexchange.Adapters.SimpleHorizontalRecycler
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    lateinit var recycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("my_trag", "Home Fragment on Create")
        val view= inflater.inflate(R.layout.fragment_home, container, false)

        recycler=view.findViewById<RecyclerView>(R.id.fragment_home_recycler)

        val horizontalRecycler=view.findViewById<RecyclerView>(R.id.fragment_home_horizontal_recycler)

        horizontalRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        horizontalRecycler.adapter = SimpleHorizontalRecycler()





        var firebaseDatabase= FirebaseDatabase.getInstance().reference
        //firebaseDatabase.child("AllBooks").addValueEventListener(valueEventListener())




        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
    }

    inner class valueEventListener: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
        val books= ArrayList<Book>()
            for(i in snapshot.children){
                for(j in  i.children){
                    val bookName=j.child("bookName").getValue(String::class.java)
                    val bookDescription=j.child("bookDescription").getValue(String::class.java)
                    val category=j.child("category").getValue(String::class.java)
                    val imageUri=j.child("imageUri").getValue(String::class.java)
                    val user=j.child("user").getValue(String::class.java)
                    val key=j.child("key").getValue(String::class.java)
                    val city=j.child("city").getValue(String::class.java)

                    books.add(
                        Book(
                            bookName!!,
                            bookDescription!!,
                            category!!,
                            imageUri!!,
                            user!!,
                            key!!,
                            city!!
                        )
                    )

                }
            }
            recycler.adapter= GridHomeAdapter(books)
            recycler.layoutManager= GridLayoutManager(activity, 2)


        }

        override fun onCancelled(error: DatabaseError) {

        }


    }

}