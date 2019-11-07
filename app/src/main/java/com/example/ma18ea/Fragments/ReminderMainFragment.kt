package com.example.ma18ea.Fragments

import android.os.Bundle
import android.system.Os.bind
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ma18ea.Calculation
import com.example.ma18ea.R
import com.example.ma18ea.ReminderVariables
import kotlinx.android.synthetic.main.fragment_reminder_main.*
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

    var timerButtonBG: Int = 1

    private var paramTitle: String? = null
    private var paramDesc: String? = null
    private var paramDoneTime: Int? = null
    private var paramTotalTime: Int? = null
    private var paramProgress: Int? = null
    private var paramDays: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calculatation = Calculation()

        arguments?.let {
            paramTitle = it.getString(ARG_PARAM_TITLE)
            paramDesc = it.getString(ARG_PARAM_DESC)
            paramDoneTime = it.getInt(ARG_PARAM_DONE_TIME)
            paramTotalTime = it.getInt(ARG_PARAM_TOTAL_TIME)
            paramProgress = calculatation.ofProgressBar(paramDoneTime!!, paramTotalTime!!).toInt()
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

        val acceptButton = frag!!.findViewById(R.id.accept_button) as Button
        var timerButton = frag!!.findViewById(R.id.timer_button) as Button
        val deleteButton = frag!!.findViewById(R.id.delete_button) as Button

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

        acceptButton.setOnClickListener {
                Log.d(TAG, "accept_button")
        }

        timerButton.setOnClickListener {
            if (timerButtonBG == 1){
                timerButtonBG = 0
                timerButton.setBackgroundResource(R.drawable.timer_button_active)
            }else{
                timerButtonBG = 1
                timerButton.setBackgroundResource(R.drawable.timer_button)
            }

            Log.d(TAG, "timerButton")
        }

        deleteButton.setOnClickListener {
            Log.d(TAG, "deleteButton")
        }


        var days = ""
        frag!!.title_reminder_txt.text = paramTitle
        frag!!.description_txt.setText(paramDesc)
        frag!!.progressbar_in_reminder.progress = this.paramProgress!!

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
