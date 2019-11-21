package com.example.ma18ea

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ma18ea.fragments.ReminderMainFragment
import com.example.ma18ea.room.Converters
import com.example.ma18ea.room.RemDatabase
import com.example.ma18ea.room.RemEntity
import com.example.ma18ea.ui.recyclerView.MainRecyclerAdapter
import kotlinx.coroutines.*
import android.os.Handler
import android.widget.NumberPicker
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.main_activity.*


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
    private var dbDown: ArrayList<RemEntity>? = null
    private var arrayRV: ArrayList<ReminderVariables> = arrayListOf()

    //RecyclerView objects
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter
    private lateinit var swipeRefresh: SwipeRefreshLayout

    //ViewPage Objects
    private lateinit var viewPager: ViewPager


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.header_toolbar))

        db = RemDatabase.getInstance(this)

        dayItem = findViewById(R.id.day_container)
        previousDay = dayItem.getChildAt(0) as TextView
        for (child in dayItem.children){
            child.setOnClickListener {
                dayItemSelected(child as TextView)
            }
        }

        viewPager = findViewById(R.id.mViewPage)

        swipeRefresh = findViewById(R.id.mSwipeRefresh)
        swipeRefresh.setOnRefreshListener { onRefresh() }

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

        GlobalScope.launch {
            fetchData()
        }

    }//On Create


    private fun onRefresh() {
        Log.d(TAG, "Is refreshing")
        GlobalScope.launch {
            fetchData()
        }
        Handler().postDelayed(Runnable { mSwipeRefresh.isRefreshing = false }, 2000)
    }


    private suspend fun fetchData()  {

        arrayRV.clear()
        dbDown?.clear()
        delay(1000)

        dbDown = db?.remDao()!!.getAll() as ArrayList<RemEntity>
        var remTemp: ReminderVariables
        for (rem in dbDown!!) {
                remTemp = ReminderVariables()
                remTemp.uid = rem.uid
                remTemp.title = rem.title.toString()
                remTemp.doneTime = rem.doneTime!!
                remTemp.totalTime = rem.totalTime!!
                remTemp.days = rem.days?.let { Converters.toList(it) }!!
                remTemp.description = rem.description.toString()

                 arrayRV.add(remTemp)
        }

        //Clears all data in the database, only for testing.
        /*for (item in dbDown!!){
            db?.remDao()?.delete(item)
        }*/

        //Check if the DB is empty or not
        Log.d(TAG, arrayRV.size.toString())

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
                var rv = ReminderVariables()
                reminderMain = ReminderMainFragment.newInstance(rv)
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






