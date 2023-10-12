package com.example.bookexchange.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.AppUtils
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.Request
import com.example.bookexchange.R
import com.google.android.play.core.integrity.i


data class itemTexts(val title:String, val state:String,val description: String)
    
    


class RequestsRecyclerAdapter(var arr:ArrayList<Request>): RecyclerView.Adapter<RequestsRecyclerAdapter.viewHolder>() {


    inner class viewHolder(view: View): RecyclerView.ViewHolder(view) {


        val title=view.findViewById<TextView>(R.id.request_title_item)
        val description=view.findViewById<TextView>(R.id.request_description_item)
        val button=view.findViewById<AppCompatButton>(R.id.button_item)
        val state=view.findViewById<TextView>(R.id.request_state_item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.requests_recycler, parent, false)

        return viewHolder(view);
    }

    override fun getItemCount(): Int {

        return arr.size;


    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.button.setOnClickListener {

        }

        val p=getTheSuitableTextAndDescription(arr[position].fromMe!!, arr[position].state.toString(),arr[position].myBooks!!,arr[position].hisBooks!!)
        holder.title.text=p.title
        holder.state.text=p.state
        holder.description.text=p.description


    }


    fun getTheSuitableTextAndDescription(fromMe:Boolean, description:String, myBooks:ArrayList<Book>, hisBooks:ArrayList<Book>):itemTexts{



        var first="Swap ";
        var second="Waiting Response"
        var third="sdsdd";


        if(description=="Received"){

            second="Decision Required"

            first="Swap Order"
            third="he wants ("
            for (i in 0 until myBooks.size-1){

                third+=myBooks[i].bookName
                third+=", "
            }


            third+=(myBooks[myBooks.size-1].bookName)
            third+=')'


            third="you wants ("
            for (i in 0 until hisBooks.size-1){

                third+=hisBooks[i].bookName
                third+=", "
            }


            third+=(hisBooks[hisBooks.size-1].bookName)
            third+=')'

        }else{
            third="he wants ( "
            second="Waiting Response"
            first="Swap Order"
            for (i in 0 until myBooks.size-1){

                third+=myBooks[i].bookName
                third+=", "
            }


            third+=(myBooks[myBooks.size-1].bookName)
            third+=')'
            third+=" you will take ("
            for (i in 0 until hisBooks.size-1){

                third+=hisBooks[i].bookName
                third+=", "
            }


            third+=(hisBooks[hisBooks.size-1].bookName)
            third+=')'


        }



        return itemTexts(first,second,third);
    }
    
    


}