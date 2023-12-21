package com.example.bookexchange.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.AppUtils
import com.example.bookexchange.UI.MakingDecisionActivity
import com.example.bookexchange.Models.Request
import com.example.bookexchange.R



    


class RequestsRecyclerAdapter(var arr:ArrayList<Request>, var context: Context): RecyclerView.Adapter<RequestsRecyclerAdapter.viewHolder>() {


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


    override fun onBindViewHolder(holder: viewHolder, position: Int) {


        AppUtils.LOG("on bind View holder")
        holder.itemView.setOnClickListener {

         Intent(context, MakingDecisionActivity::class.java).apply {

                putExtra("myKey",arr[position].myKey)
                putExtra("hisKey",arr[position].hisKey)
                putExtra("fromMe",arr[position].fromMe)
                context.startActivity(this)
            }



        }

        if(arr[position].clicked==true)
            holder.clicked.visibility=View.GONE


        val state=arr[position].state


            if(state=="RefusedByMe"){

                holder.title.text="you have cancelled the offer"
                holder.image.setImageResource(R.drawable.baseline_cancel_24)
            }else{

                if(state=="RefusedByHim") {
                    holder.title.text = "The offer is refused"
                    holder.image.setImageResource(R.drawable.baseline_cancel_24)

                }
                else if(state=="Sent") {
                    holder.title.text = "Swap Request is sent"
                }
                else{
                    if(state=="AcceptedByHim"||state=="AcceptedByMe"){

                        holder.title.text="Done ya basha";
                    }else {
                        holder.title.text = "Swap Request is received"
                    }
                }
            }



        holder.time.text=getDifferenceInTime(arr[position].date!!)


    }


    private fun getDifferenceInTime(time:Long):String{

        val timeNow=System.currentTimeMillis()
        val timeDifferenceInMillis=timeNow-time;
        AppUtils.LOG("difference in time ")
        var hours = timeDifferenceInMillis / 1000;


        if(hours<60*60){

         return "${hours/60} minutes ago "
        }else {
            if (hours < 60*60*24) {

                return "${hours/(60*60)} hours ago"
            } else {

                hours /= 24;
                return "${hours/(60*60*24)}days ago"
            }
        }

    }


    


}