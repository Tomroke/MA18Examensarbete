package com.example.ma18ea.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.EventLogTags
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ma18ea.*
import com.example.ma18ea.room.Converters
import com.example.ma18ea.room.RemDatabase
import com.example.ma18ea.room.RemEntity
import kotlinx.android.synthetic.main.fragment_reminder_main.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

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
    private lateinit var calculatation: Calculation
    private lateinit var progressBarGradientColor: ColourProgressBarGradient

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

        progressBarGradientColor = ColourProgressBarGradient()
        calculatation = Calculation()

        arguments?.let {
            paramTitle = it.getString(ARG_PARAM_TITLE)
            paramDesc = it.getString(ARG_PARAM_DESC)

            //Done time
            paramDoneTime = it.getLong(ARG_PARAM_DONE_TIME)
            //Log.d(TAG, "Done Time $paramDoneTime")

            //Total Time
            paramTotalTime = it.getLong(ARG_PARAM_TOTAL_TIME)
            //Log.d(TAG, "Total time $paramTotalTime")

            timeDifference = paramTotalTime!!.minus(paramDoneTime!!)
            //Log.d(TAG, "Difference in time $timeDifference")

            updateProgressBar()

            paramDays = it.getStringArrayList(ARG_PARAM_DAYS)
            if (paramDays.isNullOrEmpty()){
                paramDays?.add(getString(R.string.select_days_button))
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        frag = inflater.inflate(R.layout.fragment_reminder_main, container, false)

        var titleView = this.frag!!.findViewById(R.id.title_reminder_txt) as EditText
        var descriptionView = this.frag!!.findViewById(R.id.description_txt) as EditText
        val daysButton = this.frag!!.findViewById(R.id.select_days_button) as Button

        val acceptButton = this.frag!!.findViewById(R.id.accept_button) as Button
        val timerButton = this.frag!!.findViewById(R.id.timer_button) as Button
        val deleteButton = this.frag!!.findViewById(R.id.delete_button) as Button

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

        var days = ""
        for(item in paramDays!!){
            days += if(days == ""){
                item
            } else{
                ", $item"
            }
        }
        frag!!.select_days_button.text = days

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
        daysButton.setOnClickListener {
            Log.d(TAG, "daysButton")
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
            GlobalScope.async {
            acceptNewData(
                paramUID,
                paramTitle,
                paramDoneTime,
                paramTotalTime,
                paramDays,
                paramDesc)
        }}




        //Timer button
        timerButton.setOnClickListener {usingTimer(timerButton)}

        //Delete Button
        deleteButton.setOnClickListener {
            Log.d(TAG, "deleteButton")
        }



        return frag
    }


    private suspend fun acceptNewData(
        uid: Int,
        title: String?,
        doneTime: Long,
        totalTime: Long,
        days: ArrayList<String>?,
        descriptionTxt: String?){

        delay(1000)

        if(uid == 0){
            //Create Data
            var remE = RemEntity(
                uid,
                title,
                doneTime,
                totalTime,
                Converters.fromList(days!!),
                descriptionTxt)

            //Add Data
            //TODO surround this with a try catch
                db?.remDao()?.insertAll(remE)
            Log.d(TAG, "Data saved")
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
        paramProgress = calculatation.ofProgressBar(paramDoneTime!!, paramTotalTime!!).toInt()
        //Log.d(TAG, "Progress bars percent " + calculatation.ofProgressBar(paramDoneTime!!, paramTotalTime!!).toString())
        if (frag != null){
            val timerTxt: String = "" + (calculatation.toMin(paramDoneTime!!) / 60.0) + " : " + (calculatation.toMin(paramTotalTime!!) / 60.0)

            frag!!.progress_txt.text = timerTxt
            frag!!.progressbar_in_reminder.progress = this.paramProgress!!
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
