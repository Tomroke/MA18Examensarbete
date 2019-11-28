package com.example.ma18ea

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.ma18ea.fragments.ReminderMainFragment
import com.example.ma18ea.room.Converters
import com.example.ma18ea.room.RemDatabase
import com.example.ma18ea.room.RemEntity
import kotlinx.coroutines.*
import androidx.viewpager.widget.ViewPager
import com.example.ma18ea.fragments.HomeScreenFragment
import com.example.ma18ea.fragments.TemporaryFragment
import com.example.ma18ea.ui.adapters.MyFragmentPagerAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main_activity.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity()
{
    private var TAG:String  = "MAIN_ACTIVITY"
    private var context = this


    //Menu objects
    private lateinit var menu: Menu
    private var menuGroupVisible: Boolean = false

    //Fragment objects
    private lateinit var reminderMain: ReminderMainFragment
    private lateinit var tempFragment: TemporaryFragment
    private lateinit var swipeRefresh: SwipeRefreshLayout

    //Database
    private var db: RemDatabase? = null
    private var dbDown: ArrayList<RemEntity>? = null
    //private var arrayRV: ArrayList<ReminderVariables> = arrayListOf()
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    //ViewPage
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.header_toolbar))

        tabs = findViewById(R.id.tabs_layout)
        viewPager = findViewById(R.id.mViewPager)

        swipeRefresh = findViewById(R.id.mSwipeRefresh)
        swipeRefresh.setOnRefreshListener { onRefresh() }

        coroutineScope.launch(Dispatchers.Main) {
            setupViewPager(fetchDataAsync(context).await())
        }
    }//On Create


    private fun onRefresh() {
        //Log.d(TAG, "Is refreshing")
        coroutineScope.launch(Dispatchers.Main) {
            setupViewPager(fetchDataAsync(context).await())
        }
        Handler().postDelayed(Runnable { mSwipeRefresh.isRefreshing = false }, 2000)
    }


    private fun fetchDataAsync(context: Context): Deferred<ArrayList<ReminderVariables>> =
        coroutineScope.async(Dispatchers.IO) {
            val arrayRV: ArrayList<ReminderVariables> = arrayListOf()

            dbDown?.clear()
            arrayRV.clear()

            db = RemDatabase.getInstance(context)
            dbDown = db?.remDao()!!.getAll() as ArrayList<RemEntity>
            /*for (rem in dbDown!!){
                db?.remDao()!!.delete(rem)
            }*/
            //Log.d(TAG, dbDown.toString())
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
            /*Log.d(TAG, dbDown!!.size.toString())
            Log.d(TAG, "In fetchData " + dbDown.toString())*/
            Log.d(TAG, "In fetchData $arrayRV")

            return@async arrayRV
        }


            private fun setupViewPager(arrayRV: ArrayList<ReminderVariables>) {

                val adapter = MyFragmentPagerAdapter(supportFragmentManager)

                val monFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)
                val tueFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)
                val wenFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)
                val thuFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)
                val friFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)
                val satFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)
                val sunFragment: HomeScreenFragment = HomeScreenFragment.newInstance(arrayRV)

                adapter.addFragment(monFragment, "Mon")
                adapter.addFragment(tueFragment, "Tue")
                adapter.addFragment(wenFragment, "Wen")
                adapter.addFragment(thuFragment, "Thu")
                adapter.addFragment(friFragment, "Fri")
                adapter.addFragment(satFragment, "Sat")
                adapter.addFragment(sunFragment, "Sun")

                viewPager.adapter = adapter

                tabs.setupWithViewPager(viewPager)

            }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.header_menu, menu)
        this.menu = menu
        this.menu.setGroupVisible(R.id.header_menu_buttons, false)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        tempFragment = TemporaryFragment.newInstance("Temp")
        return when (item.itemId) {
            R.id.new_reminder -> {
                val rv = ReminderVariables()
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
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, tempFragment)
                    .addToBackStack(tempFragment.toString())
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                return true
            }

            R.id.header_diagram_button ->{
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, tempFragment)
                    .addToBackStack(tempFragment.toString())
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                return true
            }

            R.id.header_settings_button ->{
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, tempFragment)
                    .addToBackStack(tempFragment.toString())
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                menu.setGroupVisible(R.id.header_menu_buttons, false)
                menuGroupVisible = false
                return true
            }

            R.id.open_close_header_button ->{
                menuGroupVisible = if(menuGroupVisible){
                    menu.setGroupVisible(R.id.header_menu_buttons, false)
                    false
                } else{
                    this.menu.setGroupVisible(R.id.header_menu_buttons, true)
                    true
                }

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}