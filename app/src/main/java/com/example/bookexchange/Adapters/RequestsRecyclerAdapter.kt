package com.example.bookexchange.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.AppUtils
import com.example.bookexchange.MakingDecisionActivity
import com.example.bookexchange.Models.Book
import com.example.bookexchange.Models.Request
import com.example.bookexchange.R
import com.google.android.play.core.integrity.i


data class itemTexts(val title:String, val state:String,val description: String)
    
    


class RequestsRecyclerAdapter(var arr:ArrayList<Request>,var context: Context): RecyclerView.Adapter<RequestsRecyclerAdapter.viewHolder>() {


    inner class viewHolder(view: View): RecyclerView.ViewHolder(view) {


        val title=view.findViewById<TextView>(R.id.request_recycler_title)
        val time=view.findViewById<TextView>(R.id.request_recycler_time)
        val image=view.findViewById<ImageView>(R.id.request_recycler_image)
        val clicked=view.findViewById<ImageView>(R.id.request_recycler_clicked)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.requests_recycler, parent, false)

        return viewHolder(view);
    }

    override fun getItemCount(): Int {

        return arr.size;


    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {



        holder.itemView.setOnClickListener {

         Intent(context,MakingDecisionActivity::class.java).apply {

                putExtra("myKey",arr[position].myKey)
                putExtra("hisKey",arr[position].hisKey)
                putExtra("fromMe",arr[position].fromMe)
                context.startActivity(this)
            }



        }

        if(arr[position].clicked==true)
            holder.clicked.visibility=View.GONE


        if(arr[position].fromMe==false){

            holder.title.text="Swap Request Received"
        }else{

            holder.title.text="Swap Request Sent"
        }

        holder.time.text=getDifferenceInTime(arr[position].date!!)


    }


    fun getDifferenceInTime(time:Int):String{

        val timeNow=System.currentTimeMillis()
        val timeDifferenceInMillis=timeNow-time;
        var hours = timeDifferenceInMillis / 3600000

        if(hours<24){

            return "$hours hours ago"
        }else{

            hours /= 24;
            return "$hours days ago"
        }


    }


    


}