package com.example.ma18ea


import android.graphics.drawable.Icon
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ma18ea.Fragments.ReminderMainFragment
import com.example.ma18ea.ui.main.MainRecyclerAdapter


class MainActivity : AppCompatActivity()
{
    private var TAG:String  = "MAIN ACTIVITY"

    //Menu Variables
    private lateinit var menu: Menu
    private var menuGroupVisible: Boolean = false

    //Fragment Variables
    private lateinit var reminderMain:ReminderMainFragment

    //RecyclerView Variables
    private lateinit var arrayRV :ArrayList<ReminderVariables>
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainRecyclerAdapter


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.header_toolbar))

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

    private fun fetchData() {

        val arrayDays = ArrayList<String>()
        arrayDays.add("Monday")
        arrayDays.add("Friday")
        arrayDays.add("Sunday")

        val reminderVariables = ReminderVariables("Math", 120,180, arrayDays, "Fuga nihil voluptates sit. Voluptates incidunt porro in voluptatem omnis possimus minus nostrum. Aliquam odit illo libero sequi quasi. Nemo dignissimos odit qui est consectetur vel inventore.")
        val reminderVariables2 = ReminderVariables("Tech", 8, 120, arrayDays,"Ut odit recusandae inventore qui a omnis aperiam iure. Necessitatibus perspiciatis repellat tempora quia culpa eaque. Modi rerum voluptates cum hic et. Aut quas et deserunt illo velit deleniti. Esse illo dolorum velit quibusdam eius iusto aut vitae.")
        val reminderVariables3 = ReminderVariables("Mech", 84, 60, arrayDays,"Nemo quo non blanditiis est recusandae sunt voluptatem. At illum voluptatem illum adipisci. Tempore cupiditate et labore qui neque. Id corrupti aut tempora. Provident nihil cupiditate ea et. Est et esse minus beatae ut.")
        val reminderVariables4 = ReminderVariables("Sect", 32, 80, arrayDays,"Mollitia sed aut beatae molestiae delectus dolor excepturi. Ipsa iure soluta minima et recusandae. Hic voluptatibus doloremque nisi quisquam repudiandae officia.")

        arrayRV = arrayListOf<ReminderVariables>(
            reminderVariables,
            reminderVariables2,
            reminderVariables3,
            reminderVariables4
        )
    }//fetch Data


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






