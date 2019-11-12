package com.example.ma18ea.Fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.ma18ea.Calculation
import com.example.ma18ea.ColourProgressBarGradient
import com.example.ma18ea.R
import com.example.ma18ea.ReminderVariables
import kotlinx.android.synthetic.main.fragment_reminder_main.view.*


private const val ARG_PARAM_TITLE = "param1"
private const val ARG_PARAM_DESC = "param2"
private const val ARG_PARAM_DONE_TIME = "param3"
private const val ARG_PARAM_TOTAL_TIME = "param4"
private const val ARG_PARAM_DAYS = "param5"

class ReminderMainFragment : Fragment() {

    private val TAG : String = "RMFragment"

    private var frag: View? = null
    private lateinit var calculatation: Calculation
    private lateinit var progressBarGradientColor: ColourProgressBarGradient

    private val sec: Long = 1000
    private var isActive = false

    private var paramTitle: String? = null
    private var paramDesc: String? = null

    private var paramDoneTime: Int? = null
    private var doneTimeLong: Long = 0

    private var paramTotalTime: Int? = null
    private var totalTimeLong: Long = 0

    private var paramProgress: Int? = null
    private var paramDays: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressBarGradientColor = ColourProgressBarGradient()
        calculatation = Calculation()

        arguments?.let {
            paramTitle = it.getString(ARG_PARAM_TITLE)
            paramDesc = it.getString(ARG_PARAM_DESC)

            //Done time
            paramDoneTime = it.getInt(ARG_PARAM_DONE_TIME)
            doneTimeLong = (calculatation.toMilli(paramDoneTime!!))
            Log.d(TAG, doneTimeLong.toString())

            //Total Time
            paramTotalTime = it.getInt(ARG_PARAM_TOTAL_TIME)
            totalTimeLong = (calculatation.toMilli(paramTotalTime!!))
            Log.d(TAG, totalTimeLong.toString())

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

        val acceptButton = this.frag!!.findViewById(R.id.accept_button) as Button
        val timerButton = this.frag!!.findViewById(R.id.timer_button) as Button
        val deleteButton = this.frag!!.findViewById(R.id.delete_button) as Button

        if(paramTitle.equals("newTitle")){
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


        //Accept Button
        acceptButton.setOnClickListener {
                Log.d(TAG, "accept_button")
        }


        //Timer button
        timerButton.setOnClickListener {
            if (!isActive){
                isActive = true
                timer(totalTimeLong - doneTimeLong, sec).start()
                timerButton.setBackgroundResource(R.drawable.timer_button)
                //Log.d(TAG, "Timer Active $isCancelled")
            }else{
                timerButton.setBackgroundResource(R.drawable.timer_button_active)
                isActive = false
                //Log.d(TAG, "Timer Inactive $isCancelled")
            }
        }

        //Delete Button
        deleteButton.setOnClickListener {
            Log.d(TAG, "deleteButton")
        }


        var days = ""
        frag!!.title_reminder_txt.text = paramTitle
        frag!!.description_txt.setText(paramDesc)
        updateProgressBar()

        for(item in paramDays!!){
            days += if(days == ""){
                item
            } else{
                ", $item"
            }
        }
        frag!!.select_days_button.text = days

        return frag
    }

    private fun updateProgressBar() {
        paramProgress = calculatation.ofProgressBar(paramDoneTime!!, paramTotalTime!!).toInt()

        if (frag != null){
            val timerTxt: String = "" + (paramDoneTime!! / 60.0) + " : " + (paramTotalTime!! / 60.0)
            frag!!.progress_txt.text = timerTxt
            frag!!.progressbar_in_reminder.progress = this.paramProgress!!
        }
    }

    private fun timer(millisInFuture:Long, countDownInterval:Long):CountDownTimer{
        return object: CountDownTimer(millisInFuture, countDownInterval){
            override fun onTick(millisUntilFinished: Long){

                //Log.d(TAG, "Timer $millisUntilFinished")
                if (!isActive){
                    //Log.d(TAG, "Timer canceled")
                    doneTimeLong += calculatation.newRemainingTime(totalTimeLong, doneTimeLong, millisUntilFinished)
                    paramDoneTime = calculatation.toMin(doneTimeLong)
                    //Log.d(TAG, "time done $doneTimeLong. time remaining " + (totalTimeLong - doneTimeLong))
                    cancel()
                }
            }

            override fun onFinish() {
                doneTimeLong = totalTimeLong
                updateProgressBar()
                //Log.d(TAG, "Timer finished")
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(fragAR : ReminderVariables) =
            ReminderMainFragment().apply {

                arguments = Bundle().apply {
                    putString(ARG_PARAM_TITLE, fragAR.title)
                    putString(ARG_PARAM_DESC, fragAR.description)
                    putInt(ARG_PARAM_DONE_TIME, fragAR.doneTime)
                    putInt(ARG_PARAM_TOTAL_TIME, fragAR.totalTime)
                    putStringArrayList(ARG_PARAM_DAYS, fragAR.days)

                }
            }
    }
}
