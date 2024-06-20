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
import com.google.firebase.auth.FirebaseAuth


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


            val uiddd=FirebaseAuth.getInstance().currentUser?.uid.toString()

            Intent(context, MakingDecisionActivity::class.java).apply {

                putExtra("rid",arr[position].rid)
                putExtra("myKey", arr[position].uid1)
                putExtra("hisKey", arr[position].uid2)
                putExtra("fromMe", arr[position].uid1==uiddd)
                context.startActivity(this)
            }

        }

        val uiddd=FirebaseAuth.getInstance().currentUser?.uid.toString()
        if(arr[position].clicked==true)
            holder.clicked.visibility=View.GONE

        val fromMe=arr[position].uid1==uiddd

        val state=arr[position].rstate


        if(state=="pending"&&fromMe){

                holder.title.text="waiting the response"
                holder.image.setImageResource(R.drawable.baseline_swap_vertical_circle_24)
            }else {

            if (state == "pending" && !fromMe) {

                holder.title.text="you received a request"
                holder.image.setImageResource(R.drawable.baseline_subdirectory_arrow_left_24)

            } else {
                if (state == "accepted") {
                    holder.title.text = "Completed Request ^_^"
                    holder.image.setImageResource(R.drawable.baseline_done_outline_24)

                } else if (state == "refused") {
                    holder.title.text = "A Cancelled Request"
                    holder.image.setImageResource(R.drawable.baseline_cancel_24)
                }
            }

        }




        holder.time.text=getDifferenceInTime(arr[position].rdate.toString().toLong())

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