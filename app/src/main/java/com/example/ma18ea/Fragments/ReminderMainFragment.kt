package com.example.ma18ea.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.ma18ea.*
import com.example.ma18ea.room.Converters
import com.example.ma18ea.room.RemDatabase
import com.example.ma18ea.room.RemEntity
import kotlinx.android.synthetic.main.fragment_reminder_main.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.concurrent.TimeUnit


private const val ARG_PARAM_UID = "param0"
private const val ARG_PARAM_TITLE = "param1"
private const val ARG_PARAM_DESC = "param2"
private const val ARG_PARAM_DONE_TIME = "param3"
private const val ARG_PARAM_TOTAL_TIME = "param4"
private const val ARG_PARAM_DAYS = "param5"

class ReminderMainFragment : Fragment() {

    private val TAG : String = "RMFragment"

    private var frag: View? = null
    private var newRem = false
    private var fragCom:FragCommunicator? = null

    private lateinit var calculatation: Calculation
    private lateinit var dayFrag: DayFragment

    private var min = 0
    private var hour = 0

    private val sec: Long = 1000
    private var isActive = false

    private var paramUID: Int = 0
    private var paramTitle: String? = null
    private var paramDesc: String? = null

    private var paramDoneTime: Long = 0
    private var paramTotalTime: Long = 0
    private var timeDifference: Long = 0

    private var paramProgress: Int? = null
    private var paramDays: ArrayList<String>? = null

    private var db: RemDatabase? = null


    companion object {
        @JvmStatic
        fun newInstance(fragAR : ReminderVariables) =
            ReminderMainFragment().apply {

                arguments = Bundle().apply {
                    putInt(ARG_PARAM_UID, fragAR.uid)
                    putString(ARG_PARAM_TITLE, fragAR.title)
                    putString(ARG_PARAM_DESC, fragAR.description)
                    putLong(ARG_PARAM_DONE_TIME, fragAR.doneTime)
                    putLong(ARG_PARAM_TOTAL_TIME, fragAR.totalTime)
                    putStringArrayList(ARG_PARAM_DAYS, fragAR.days)

                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = RemDatabase.getInstance(context!!)
        fragCom = ViewModelProviders.of(activity!!).get(FragCommunicator::class.java)

        calculatation = Calculation()

        arguments?.let {
            paramUID = it.getInt(ARG_PARAM_UID)
            //Log.d(TAG, "UID: " + paramUID)

            paramTitle = it.getString(ARG_PARAM_TITLE)
            //Log.d(TAG, "Title: " + paramTitle)

            paramDesc = it.getString(ARG_PARAM_DESC)
            //Log.d(TAG, "Description: " + paramDesc)

            //Done time
            paramDoneTime = it.getLong(ARG_PARAM_DONE_TIME)
            //Log.d(TAG, "Done Time $paramDoneTime")

            //Total Time
            paramTotalTime = it.getLong(ARG_PARAM_TOTAL_TIME)
            //Log.d(TAG, "Total time $paramTotalTime")

            timeDifference = paramTotalTime.minus(paramDoneTime)
            //Log.d(TAG, "Difference in time $timeDifference")

            updateProgressBar()

            paramDays = it.getStringArrayList(ARG_PARAM_DAYS)
            //Log.d(TAG, "Days: " + paramDays.toString())

        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        frag = inflater.inflate(R.layout.fragment_reminder_main, container, false)

        val titleView = this.frag!!.findViewById(R.id.title_reminder_txt) as EditText
        val descriptionView = this.frag!!.findViewById(R.id.description_txt) as EditText

        val daysButton = this.frag!!.findViewById(R.id.select_days_button) as Button
        val acceptButton = this.frag!!.findViewById(R.id.accept_button) as Button
        val timerButton = this.frag!!.findViewById(R.id.timer_button) as Button
        val deleteButton = this.frag!!.findViewById(R.id.delete_button) as Button

        val minPick = this.frag!!.findViewById(R.id.mMinPicker) as NumberPicker

        calHoursMins()
        minPick.maxValue = 60
        minPick.minValue = 0
        minPick.wrapSelectorWheel = true
        minPick.value = min
        minPick.setOnValueChangedListener { picker, oldVal, newVal ->
            min = newVal
            setNewTime()
        }

        val hourPick = this.frag!!.findViewById(R.id.mHourPicker) as NumberPicker
        hourPick.maxValue = 24
        hourPick.minValue = 0
        hourPick.wrapSelectorWheel = true
        hourPick.value = hour
        hourPick.setOnValueChangedListener { picker, oldVal, newVal ->
            hour = newVal
            setNewTime()
        }

        
        if(paramTitle.equals("newTitle")){
            newRem = true
        }

        if(newRem){
            paramTitle = "Title"

            timerButton.visibility = View.INVISIBLE
            timerButton.isClickable = false
            deleteButton.visibility = View.INVISIBLE
            deleteButton.isClickable = false
        }else{
            timerButton.visibility = View.VISIBLE
            timerButton.isClickable = true
            deleteButton.visibility = View.VISIBLE
            deleteButton.isClickable = true
        }

        frag!!.title_reminder_txt.setText(paramTitle)
        frag!!.description_txt.setText(paramDesc)
        updateProgressBar()
        updateDayButton()

        //Title
        titleView.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(s: Editable) {
                paramTitle = s.toString()
            }


            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })

        //Day Button
        daysButton.background.setColorFilter(
            Color.parseColor("#4043464B"),
            PorterDuff.Mode.SCREEN)

        daysButton.setOnClickListener {

            dayFrag = DayFragment.newInstance(paramDays!!)
            fragmentManager!!
                .beginTransaction()
                .replace(R.id.container, dayFrag)
                .addToBackStack(dayFrag.toString())
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }

        //Description
        descriptionView.addTextChangedListener(object: TextWatcher{

            override fun afterTextChanged(s: Editable) {
                paramDesc = s.toString()
            }


            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {}
        })


        //Accept Button
        acceptButton.setOnClickListener {
            GlobalScope.launch { acceptNewData() }

            val inputMethodManager = activity?.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken, 0
            )
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }


        //Timer button
        timerButton.setOnClickListener {usingTimer(timerButton)}


        //Delete Button
        deleteButton.setOnClickListener { GlobalScope.launch { deleteData() }

            val inputMethodManager = activity?.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                activity?.currentFocus?.windowToken, 0
            )
            fragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        return frag
    }


    override fun onResume() {
        super.onResume()
            fragCom?.days?.observe(this,
                Observer<Any> {
                        o ->
                    //Log.d(TAG, "In onResume; in Observer: $o")
                    paramDays = Converters.toList(o.toString()) })
            //Log.d(TAG, paramDays.toString())
            updateDayButton()
    }


    override fun onPause() {
        super.onPause()
        if (paramDays!![0] != "Select Days"){
            fragCom!!.setDays(Converters.fromList(paramDays!!))
        }
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "in onStop")
        GlobalScope.launch { acceptNewData() }
    }

    private fun setNewTime(){
        paramTotalTime = (Calculation().toMilli(hour * 60) + Calculation().toMilli(min))
        updateProgressBar()
        Log.d(TAG, "new total time $paramTotalTime")
    }


    private fun calHoursMins(){
        var minutes = TimeUnit.MILLISECONDS.toMinutes(paramTotalTime)
        var hours = TimeUnit.MILLISECONDS.toHours(paramTotalTime)
        minutes -= (hours * 60)
        min = minutes.toInt()
        hour = hours.toInt()
        //Log.d(TAG, "Minutes: $min")
        //Log.d(TAG, "Hours: $hour")
    }


    private fun deleteData(){
        val delRem = RemEntity(
            paramUID,
            paramTitle,
            paramDoneTime,
            paramTotalTime,
            Converters.fromList(paramDays!!),
            paramDesc)

        db?.remDao()?.delete(delRem)

    }


    private suspend fun acceptNewData(){

        delay(1000)

        val uidCheck = db?.remDao()?.findByID(paramUID)
        val remE: RemEntity
        if (uidCheck?.uid == paramUID){
            //edit data
            remE = RemEntity(
                paramUID,
                paramTitle,
                paramDoneTime,
                paramTotalTime,
                Converters.fromList(paramDays!!),
                paramDesc
            )

            //update Data
            try {
                db?.remDao()?.updateData(remE)
                //Log.d(TAG, "Data edited and saved")
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }else {
            //Create Data
            remE = RemEntity(
                GenUID().generateUID(),
                paramTitle,
                paramDoneTime,
                paramTotalTime,
                Converters.fromList(paramDays!!),
                paramDesc
            )

            //Add Data
            try {
                db?.remDao()?.insertAll(remE)
                //Log.d(TAG, "Data created and saved")
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }

    }


    private fun updateDayButton(){
        if (paramDays?.get(0) != "Select Days"){
            var days = ""
            for(item in paramDays!!){
                Log.d(TAG, "")
                days += when {
                    days == "" -> item
                    item == paramDays!!.last() -> ", $item"
                    else -> ", $item"
                }
                frag!!.select_days_button.text = days
            }
        }
        else{
            frag!!.select_days_button.text = paramDays?.get(0)
        }

    }


    private fun usingTimer(timerButton: View){
        if (!isActive){
            timerButton.setBackgroundResource(R.drawable.timer_button_active)
            isActive = true
            timer(timeDifference).start()
            //Log.d(TAG, "Timer Active $isActive")
        }else{
            timerButton.setBackgroundResource(R.drawable.timer_button)
            isActive = false
            //Log.d(TAG, "Timer Active $isActive")
        }
    }


    private fun updateProgressBar() {
        paramProgress = calculatation.ofProgressBar(paramDoneTime, paramTotalTime).toInt()
        Log.d(TAG, "Progress bars percent $paramProgress")
        if (frag != null){
            frag!!.progressbar_in_reminder.progress = paramProgress!!
        }
    }


    private fun timer(millisInFuture:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture, sec){
            override fun onTick(millisUntilFinished: Long){

                //Log.d(TAG, "Timer $millisUntilFinished")
                if (!isActive){
                    paramDoneTime += timeDifference - millisUntilFinished
                    updateProgressBar()
                    //Log.d(TAG, "Timer canceled")
                    //Log.d(TAG, "Time done $paramDoneTime.")
                    cancel()
                }
            }

            override fun onFinish() {
                paramDoneTime = paramTotalTime
                updateProgressBar()
                //Log.d(TAG, "Timer finished")
            }
        }

    }
}
