package com.example.bookexchange.UI

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.Adapters.BooksListener
import com.example.bookexchange.Adapters.DialogFragmentRecycler
import com.example.bookexchange.Models.Book
import com.example.bookexchange.R
import com.example.bookexchange.ViewModels.MyBooksViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


interface FinishingFragmentListener{

    fun setListener(arr:ArrayList<Book>);
}
class ChoosingBooksDialog: DialogFragment(),BooksListener {

    lateinit var button:AppCompatButton
    lateinit var myBooksViewModel: MyBooksViewModel
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var finishingFragmentListener: FinishingFragmentListener
    lateinit var adapter:DialogFragmentRecycler
    lateinit var books :ArrayList<Book>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.choosing_book_dialog, null)
        val recycler=view.findViewById<RecyclerView>(R.id.dialog_recycler)

        firebaseAuth=FirebaseAuth.getInstance()
        button=view.findViewById(R.id.dialog_button)

        builder.setView(view);

        myBooksViewModel= ViewModelProvider(this)[MyBooksViewModel::class.java]

        myBooksViewModel.myBooksList.observe(this){
            books=it
            adapter=DialogFragmentRecycler(books,firebaseAuth.currentUser!!.uid,requireContext())
            adapter.setListener(this)
            recycler.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter=adapter;

        }
        button.setOnClickListener {
            val chosenBooks=ArrayList<Book>()
            for (i in adapter.chosenBooks){
                chosenBooks.add(books[i]);
            }
            finishingFragmentListener.setListener(chosenBooks)


        }




        GlobalScope.launch(Dispatchers.IO) {

            myBooksViewModel.readTexts(firebaseAuth.currentUser!!.uid);

        }

        return builder.create();
    }
    fun setListener(finishingFragmentListener: FinishingFragmentListener){
        this.finishingFragmentListener=finishingFragmentListener;

    }

    override fun onItemClick(size: Int) {
        button.isEnabled = size!=0;
    }
}