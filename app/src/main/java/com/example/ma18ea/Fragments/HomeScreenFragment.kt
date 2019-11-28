package com.example.ma18ea.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ma18ea.IOnBackPressed
import com.example.ma18ea.R
import com.example.ma18ea.ReminderVariables
import com.example.ma18ea.ui.adapters.MainRecyclerAdapter

class HomeScreenFragment : Fragment() {
    private val TAG: String = "HOME_SCREEN_FRAGMENT"

    private var arrayRV: ArrayList<ReminderVariables> = ArrayList()

    //Fragment objects
    private lateinit var reminderMain: ReminderMainFragment

    //RecyclerView objects
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter



    companion object {
        @JvmStatic
        fun newInstance(array: ArrayList<ReminderVariables>) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    var remTemp: ReminderVariables
                    for (rem in array){
                        remTemp = ReminderVariables()
                        remTemp.uid = rem.uid
                        remTemp.title = rem.title
                        remTemp.doneTime = rem.doneTime
                        remTemp.totalTime = rem.totalTime
                        remTemp.days = rem.days
                        remTemp.description = rem.description
                        arrayRV.add(remTemp)
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frag = inflater.inflate(R.layout.fragment_home_screen, container, false)

            recyclerView = frag.findViewById<RecyclerView?>(R.id.mRecyclerView) as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this.context)
            adapter = MainRecyclerAdapter(arrayRV)
            recyclerView.adapter = adapter

            recyclerView.addOnItemClickListener(object : OnItemClickListener {
                override fun onItemClicked(position: Int, view: View) {

                    reminderMain = ReminderMainFragment.newInstance(arrayRV[position])
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, reminderMain)
                        .addToBackStack(reminderMain.toString())
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit()
                }
            })

        return frag
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }


    private fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)

                }
            }
        })
    }

}