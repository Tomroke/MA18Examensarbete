package com.example.ma18ea

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ma18ea.Fragments.ReminderMainFragment
import com.example.ma18ea.ui.main.MainRecyclerAdapter
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity()
{
    private var TAG:String  = "MAIN ACTIVITY"

    private lateinit var reminderMain:ReminderMainFragment

    private lateinit var arrayRV :ArrayList<ReminderVariables>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



        fetchData()

        try {
            recyclerView = findViewById<RecyclerView>(R.id.mRecyclerView) as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = MainRecyclerAdapter(arrayRV)
            recyclerView.adapter = adapter

                recyclerView.addOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClicked(position: Int, view: View) {

                        reminderMain = ReminderMainFragment.newInstance(arrayRV[position])
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, reminderMain)
                            .addToBackStack(reminderMain.toString())
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit()
                    }
                })} catch (e: Exception) {
                Log.e(TAG, e.message)
            }

        }//On Create

    private fun fetchData() {

        val reminderVariables = ReminderVariables("Math", "4", 20)
        val reminderVariables2 = ReminderVariables("Tech", "8", 50)
        val reminderVariables3 = ReminderVariables("Mech", "16", 75)
        val reminderVariables4 = ReminderVariables("Sect", "32", 10)

        arrayRV = arrayListOf<ReminderVariables>(
            reminderVariables,
            reminderVariables2,
            reminderVariables3,
            reminderVariables4
        )

    }

}



interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}

fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view?.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    })
}

