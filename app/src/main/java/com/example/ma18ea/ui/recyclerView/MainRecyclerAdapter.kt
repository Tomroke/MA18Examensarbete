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
import kotlinx.android.synthetic.main.recycler_view_item.view.*

class MainRecyclerAdapter(private val arrayRV : ArrayList<ReminderVariables>): RecyclerView.Adapter<ViewHolder>() {
    private val TAG : String = "MainRecyclerAdapter"
    private lateinit var calculatation: Calculation
    private lateinit var progressBarGradientColor: ColourProgressBarGradient

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
        val progress = calculatation.ofProgressBar(arrayRV[position].doneTime, arrayRV[position].totalTime).toInt()

        progressBarGradientColor = ColourProgressBarGradient()
        val colour = progressBarGradientColor.getColour(progress)

        var bar = holder.itemView.recycler_progressBar.findViewById<ProgressBar?>(R.id.recycler_progressBar) as ProgressBar

        var drawable = bar.progressDrawable
        drawable.setColorFilter(Color.MAGENTA, PorterDuff.Mode.MULTIPLY)
        bar.progressDrawable = drawable
        holder.itemView.recycler_progressBar.indeterminateTintList = ColorStateList.valueOf(Color.RED)



        holder.itemView.recycler_title_txt?.text = arrayRV[position].title
        holder.itemView.recycler_timer_txt.text = calculatation.ofTimeInHours(arrayRV[position].doneTime, arrayRV[position].totalTime)
        holder.itemView.recycler_progressBar.progress = progress
    }

}

class ViewHolder (mView: View): RecyclerView.ViewHolder(mView){}