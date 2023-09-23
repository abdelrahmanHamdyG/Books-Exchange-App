package com.example.bookexchange.Adapters

import android.annotation.SuppressLint
import android.view.KeyEvent.ACTION_DOWN
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.AppUtils
import com.example.bookexchange.R

class SimpleHorizontalRecycler(): RecyclerView.Adapter<SimpleHorizontalRecycler.viewHolder>() {

    val drawableArray = arrayOf(
        R.drawable.all_inclusive,
        R.drawable.islamic,
        R.drawable.biography,
        R.drawable.history,
        R.drawable.dictionary,
        R.drawable.novel,
        R.drawable.science,
        // Add more drawable resource IDs as needed
    )
    val texts = arrayOf(
        "All",
        "Islamic",
        "Biography",
        "History",
        "Dictionary",
        "Novel",
        "Science",

    )
    var pressed = arrayOf(
        1,
        0,
        0,
        0,
        0,
        0,
        0
    );
    val dr= arrayOf(
        R.drawable.categories_borders_pressed,
        R.drawable.categories_borders_enabled
    )

    inner class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView){


        val text=itemView.findViewById<TextView>(R.id.horizontal_grid_text)
        val image=itemView.findViewById<ImageView>(R.id.horizontal_grid_image)
        val layout=itemView.findViewById<LinearLayout>(R.id.horizontal_grid_layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.horizontal_recycler, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {

        return 7;
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.image.setImageResource(drawableArray[position])
        holder.text.text=texts[position]



        if(position==0){


                holder.itemView.setBackgroundResource(dr[pressed[0]])

        }

        if(pressed[0]==1&&position>0){

            holder.itemView.setBackgroundResource(R.drawable.categories_borders_pressed)
        }
        if(pressed[0]==0&&position>0){
            holder.itemView.setBackgroundResource(dr[pressed[position]])

        }


        holder.itemView.setOnClickListener{

            if(position==0){
                    if(pressed[0]==0){
                        pressed[0]=1;
                        for (i in 1..6){
                            pressed[i]=0;
                        }

                    }else{
                        pressed[0]=0;
                        pressed[1]=1;


                    }

            }else{
                if(pressed[0]==1){

                    pressed[0]=0
                    pressed[position]=pressed[position]*-1+1;

                }else{
                    pressed[position]=pressed[position]*-1+1;
                }


            }
            notifyDataSetChanged()
        }


    }


}