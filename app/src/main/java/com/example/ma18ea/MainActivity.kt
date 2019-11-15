package com.example.ma18ea

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ma18ea.fragments.ReminderMainFragment
import com.example.ma18ea.room.Converters
import com.example.ma18ea.room.RemDatabase
import com.example.ma18ea.room.RemEntity
import com.example.ma18ea.ui.recyclerView.MainRecyclerAdapter
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.coroutines.suspendCoroutine


class MainActivity : AppCompatActivity()
{
    private var TAG:String  = "MAIN ACTIVITY"

    //Menu objects
    private lateinit var menu: Menu
    private var menuGroupVisible: Boolean = false

    //Day objects
    private lateinit var dayItem:LinearLayout
    private lateinit var previousDay:TextView

    //Fragment objects
    private lateinit var reminderMain:ReminderMainFragment

    //Database
    private var db: RemDatabase? = null

    //RecyclerView objects
    private lateinit var arrayRV :ArrayList<ReminderVariables>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.header_toolbar))

        Log.d(TAG, GenUID().generateUID().toString())
        db = RemDatabase.getInstance(this)

        dayItem = findViewById(R.id.day_container)
        previousDay = dayItem.getChildAt(0) as TextView
        for (child in dayItem.children){
            child.setOnClickListener {
                dayItemSelected(child as TextView)
            }
        }

        val arrayDays = ArrayList<String>()
        arrayDays.add("Monday")
        arrayDays.add("Friday")
        arrayDays.add("Sunday")

        val rV = ReminderVariables(
            5,
            "Math",
            7200000,
            10800000,
            arrayDays,
            "Fuga nihil voluptates sit. Voluptates incidunt porro in voluptatem omnis possimus" +
                    " minus nostrum. Aliquam odit illo libero sequi quasi. Nemo dignissimos odit qui est " +
                    "consectetur vel inventore.")

        arrayRV = arrayListOf(rV)

        try {
            recyclerView = findViewById<RecyclerView?>(R.id.mRecyclerView) as RecyclerView
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

        GlobalScope.async {
            fetchData()
        }

    }//On Create


    private suspend fun fetchData()  {

        //Create Data
        /*var remE = RemEntity(
            arrayRV[0].uid,
            arrayRV[0].title,
            arrayRV[0].doneTime,
            arrayRV[0].totalTime,
            Converters.fromList(arrayRV[0].days),
            arrayRV[0].description)

        //Add Data
        db?.remDao()?.insertAll(remE)*/

        //Delete Data
        //db?.remDao()?.delete(RemEntity(6, "Math", 7200000, 10800000,"Monday" /*Converters.fromList(arrayDays)*/, "Fuga nihil voluptates sit."))


        delay(1000)


    }//fetch Data


    private fun dayItemSelected(item: TextView){
        if (item != previousDay){
            item.setBackgroundResource(R.drawable.day_selected)
            item.setTextColor(resources.getColor(R.color.selectedDayColour))
            previousDay.setBackgroundResource(R.drawable.day_deselected)
            previousDay.setTextColor(resources.getColor(R.color.dayTextColour))
            previousDay = item
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        this.menu = menu
        this.menu.setGroupVisible(R.id.header_menu_buttons, false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_reminder -> {
                reminderMain = ReminderMainFragment.newInstance(ReminderVariables())
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, reminderMain)
                    .addToBackStack(reminderMain.toString())
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                true
            }

            R.id.header_search_button ->{
                Toast.makeText(applicationContext, "Search", Toast.LENGTH_LONG).show()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                return true
            }

            R.id.header_diagram_button ->{
                Toast.makeText(applicationContext, "Diagram", Toast.LENGTH_LONG).show()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                return true
            }

            R.id.header_settings_button ->{
                Toast.makeText(applicationContext, "Settings", Toast.LENGTH_LONG).show()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                return true
            }

            R.id.open_close_header_button ->{
                if(menuGroupVisible){
                    menu.setGroupVisible(R.id.header_menu_buttons, false)
                    menuGroupVisible = false
                }
                else{
                    this.menu.setGroupVisible(R.id.header_menu_buttons, true)
                    menuGroupVisible = true
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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

                    //Closes the header menu
                    menu.setGroupVisible(R.id.header_menu_buttons, false)
                    menuGroupVisible = false
                }
            }
        })
    }


}






