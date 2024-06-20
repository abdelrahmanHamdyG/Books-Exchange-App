package com.example.bookexchange.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.bookexchange.AppUtils.texts
import com.example.bookexchange.R

interface OnItemClickListener {
    fun onItemClick(mask: Array<Int>)
}
class SimpleHorizontalRecycler(): RecyclerView.Adapter<SimpleHorizontalRecycler.viewHolder>() {
    private var onItemClickListener: OnItemClickListener? = null


    private val drawableArray = arrayOf(
        R.drawable.all_inclusive,
        R.drawable.islamic,
        R.drawable.biography,
        R.drawable.history,
        R.drawable.dictionary,
        R.drawable.novel,
        R.drawable.science,
        R.drawable.baseline_star_24
        // Add more drawable resource IDs as needed
    )

    private var pressed = arrayOf(
        1,
        0,
        0,
        0,
        0,
        0,
        0,
        0
    );
    private val dr= arrayOf(
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

        return 8;
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
                        for (i in 1..7){
                            pressed[i]=0;
                        }

                    }else{


                        pressed[0]=0;
                        pressed[1]=1;

                    }

            }else{
                if(position==7){
                    if(pressed[7]==0){
                        pressed[7]=1
                        for(i in 0..6)
                            pressed[i]=0;

                    }else{
                        pressed[7]=0;
                        pressed[0]=1;

                    }


                }else {
                    if (pressed[0] == 1) {

                        pressed[0] = 0
                        pressed[position] = pressed[position] * -1 + 1;

                    } else {
                        if(pressed[7]==1){
                            pressed[7]=0;
                            pressed[position]=pressed[position]*-1+1
                        }else {

                            pressed[position] = pressed[position] * -1 + 1;
                        }
                    }
                }
            }




            onItemClickListener!!.onItemClick(pressed)
            notifyDataSetChanged()

        }

    }

    fun setItemClickListener(listener: OnItemClickListener){

        this.onItemClickListener=listener
    }


}