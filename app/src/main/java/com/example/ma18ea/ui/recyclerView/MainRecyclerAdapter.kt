package com.example.ma18ea.ui.recyclerView

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.ma18ea.Calculation
import com.example.ma18ea.ColourProgressBarGradient
import com.example.ma18ea.R
import com.example.ma18ea.ReminderVariables
import com.example.ma18ea.room.RemEntity
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class MainRecyclerAdapter(private val arrayRV: List<ReminderVariables>?): RecyclerView.Adapter<ViewHolder>() {
    private val TAG : String = "MainRecyclerAdapter"
    private lateinit var calculatation: Calculation

    override fun getItemCount(): Int {
        return arrayRV!!.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val cellForRow = layoutInflater.inflate(R.layout.recycler_view_item, parent, false)

        return ViewHolder(cellForRow)
    }


    //This is where you add and change the object in the recycler view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //dayButtonArray[6].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)

        calculatation = Calculation()
        val progress = calculatation.ofProgressBar(
            arrayRV?.get(position)?.doneTime,
            arrayRV?.get(position)?.totalTime
        ).toInt()

        /*val bar = holder.itemView.recycler_progressBar.findViewById<ProgressBar?>(R.id.recycler_progressBar) as ProgressBar

        val drawable = bar.progressDrawable
        drawable.setColorFilter(Color.parseColor("#494444"), PorterDuff.Mode.SCREEN)
        drawable.setColorFilter(Color.parseColor("#EBBA15"), PorterDuff.Mode.MULTIPLY)
        bar.progressDrawable = drawable
        holder.itemView.recycler_progressBar.indeterminateTintList = ColorStateList.valueOf(Color.RED)
*/


        holder.itemView.recycler_title_txt?.text = arrayRV?.get(position)?.title
        holder.itemView.recycler_timer_txt.text = calculatation.ofTimeInHours(
            arrayRV?.get(position)?.doneTime,
            arrayRV?.get(position)?.totalTime)
        holder.itemView.recycler_progressBar.progress = progress
    }

}

class ViewHolder (mView: View): RecyclerView.ViewHolder(mView){}