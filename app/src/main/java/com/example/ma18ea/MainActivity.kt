package com.example.ma18ea


import android.content.Context
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ma18ea.fragments.ReminderMainFragment
import com.example.ma18ea.room.RoomEntity
import com.example.ma18ea.room.RoomViewModel
import com.example.ma18ea.ui.recyclerView.MainRecyclerAdapter


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

    //RecyclerView objects
    private lateinit var arrayRV :ArrayList<ReminderVariables>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter

    //Room objects
    private lateinit var roomViewModel: RoomViewModel
    private var words = emptyList<RoomEntity>() // Cached copy of words


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.header_toolbar))

        roomViewModel = ViewModelProviders.of(this).get(RoomViewModel::class.java)
        roomViewModel.allWords.observe(this, Observer {
                words -> words?.let { setWords(it) }
        })


        dayItem = findViewById(R.id.day_container)
        previousDay = dayItem.getChildAt(0) as TextView
        for (child in dayItem.children){
            child.setOnClickListener {
                dayItemSelected(child as TextView)
            }
        }

        fetchData()

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

    }//On Create

    internal fun setWords(words: List<RoomEntity>) {
        this.words = words
    }

    private fun fetchData() {

        //Temp data
        val arrayDays = ArrayList<String>()
        arrayDays.add("Monday")
        arrayDays.add("Friday")
        arrayDays.add("Sunday")

        val rV = ReminderVariables("Math", 120,180, arrayDays, "Fuga nihil voluptates sit. Voluptates incidunt porro in voluptatem omnis possimus minus nostrum. Aliquam odit illo libero sequi quasi. Nemo dignissimos odit qui est consectetur vel inventore.")
        val rV2 = ReminderVariables("Tech", 8, 120, arrayDays,"Ut odit recusandae inventore qui a omnis aperiam iure. Necessitatibus perspiciatis repellat tempora quia culpa eaque. Modi rerum voluptates cum hic et. Aut quas et deserunt illo velit deleniti. Esse illo dolorum velit quibusdam eius iusto aut vitae.")
        val rV3 = ReminderVariables("Mech", 84, 60, arrayDays,"Nemo quo non blanditiis est recusandae sunt voluptatem. At illum voluptatem illum adipisci. Tempore cupiditate et labore qui neque. Id corrupti aut tempora. Provident nihil cupiditate ea et. Est et esse minus beatae ut.")
        val rV4 = ReminderVariables("Sect", 32, 80, arrayDays,"Mollitia sed aut beatae molestiae delectus dolor excepturi. Ipsa iure soluta minima et recusandae. Hic voluptatibus doloremque nisi quisquam repudiandae officia.")
        val rV5 = ReminderVariables("Math", 120,180, arrayDays, "Fuga nihil voluptates sit. Voluptates incidunt porro in voluptatem omnis possimus minus nostrum. Aliquam odit illo libero sequi quasi. Nemo dignissimos odit qui est consectetur vel inventore.")
        val rV6 = ReminderVariables("Tech", 8, 120, arrayDays,"Ut odit recusandae inventore qui a omnis aperiam iure. Necessitatibus perspiciatis repellat tempora quia culpa eaque. Modi rerum voluptates cum hic et. Aut quas et deserunt illo velit deleniti. Esse illo dolorum velit quibusdam eius iusto aut vitae.")
        val rV7 = ReminderVariables("Mech", 84, 60, arrayDays,"Nemo quo non blanditiis est recusandae sunt voluptatem. At illum voluptatem illum adipisci. Tempore cupiditate et labore qui neque. Id corrupti aut tempora. Provident nihil cupiditate ea et. Est et esse minus beatae ut.")
        val rV8 = ReminderVariables("Sect", 32, 80, arrayDays,"Mollitia sed aut beatae molestiae delectus dolor excepturi. Ipsa iure soluta minima et recusandae. Hic voluptatibus doloremque nisi quisquam repudiandae officia.")
        val rV9 = ReminderVariables("Math", 120,180, arrayDays, "Fuga nihil voluptates sit. Voluptates incidunt porro in voluptatem omnis possimus minus nostrum. Aliquam odit illo libero sequi quasi. Nemo dignissimos odit qui est consectetur vel inventore.")
        val rV10 = ReminderVariables("Tech", 8, 120, arrayDays,"Ut odit recusandae inventore qui a omnis aperiam iure. Necessitatibus perspiciatis repellat tempora quia culpa eaque. Modi rerum voluptates cum hic et. Aut quas et deserunt illo velit deleniti. Esse illo dolorum velit quibusdam eius iusto aut vitae.")
        val rV11 = ReminderVariables("Mech", 84, 60, arrayDays,"Nemo quo non blanditiis est recusandae sunt voluptatem. At illum voluptatem illum adipisci. Tempore cupiditate et labore qui neque. Id corrupti aut tempora. Provident nihil cupiditate ea et. Est et esse minus beatae ut.")
        val rV12 = ReminderVariables("Sect", 32, 80, arrayDays,"Mollitia sed aut beatae molestiae delectus dolor excepturi. Ipsa iure soluta minima et recusandae. Hic voluptatibus doloremque nisi quisquam repudiandae officia.")

        arrayRV = arrayListOf(
            rV, rV2, rV3, rV4, rV5, rV6, rV7, rV8, rV9, rV10, rV11, rV12
        )
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
                for (str in words){
                    Log.d(TAG, str.title)
                    Log.d(TAG, str.word)
                }
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






