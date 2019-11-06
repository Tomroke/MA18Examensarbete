package com.example.ma18ea.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ma18ea.Calculation
import com.example.ma18ea.R
import com.example.ma18ea.ReminderVariables
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class MainRecyclerAdapter(private val arrayRV : ArrayList<ReminderVariables>): RecyclerView.Adapter<ViewHolder>() {
    private val TAG : String = "MainRecyclerAdapter"
    private lateinit var calculatation: Calculation

    override fun getItemCount(): Int {
        return arrayRV.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val cellForRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)

        return ViewHolder(cellForRow)
    }


    //This is where you add and change the object in the recycler view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        calculatation = Calculation()

        holder.itemView.recycler_title_txt?.text = arrayRV[position].title
        holder.itemView.recycler_timer_txt.text = calculatation.ofTimeInHours(arrayRV[position].doneTime, arrayRV[position].totalTime)
        holder.itemView.recycler_progressBar.progress = calculatation.ofProgressBar(arrayRV[position].doneTime, arrayRV[position].totalTime).toInt()
    }

}

class ViewHolder (mView: View): RecyclerView.ViewHolder(mView){}