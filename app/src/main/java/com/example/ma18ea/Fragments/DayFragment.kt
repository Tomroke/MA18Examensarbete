package com.example.ma18ea.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.ma18ea.R
import com.example.ma18ea.room.Converters
import kotlinx.android.synthetic.main.select_days.*

const val ARG_DAY_COUNT = "item_count"

class DayFragment : BottomSheetDialogFragment() {
    private val TAG: String = "DayFragment"

    private var frag: View? = null

    private var fragCom: FragCommunicator? = null

    //Arrays
    private lateinit var dayButtonArray: ArrayList<Button>
    private var returnStrings: ArrayList<String>? = null

    //Buttons
    private var monBool: Boolean = false
    private var tueBool: Boolean = false
    private var wenBool: Boolean = false
    private var thuBool: Boolean = false
    private var friBool: Boolean = false
    private var satBool: Boolean = false
    private var sunBool: Boolean = false

    //Button Colours
    private var notSelColour: String = "#4043464B"
    private var selColour: String = "#9043464B"

    companion object {
        fun newInstance(itemCount: ArrayList<String>): DayFragment =
            DayFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_DAY_COUNT, itemCount)
                }
            }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        frag = inflater.inflate(R.layout.select_days, day_fragment_container, false)
        //Log.d(TAG, "In onCreateView")

        fragCom= ViewModelProviders.of(activity!!).get(FragCommunicator::class.java)

        returnStrings = ArrayList()

        dayButtonArray = ArrayList()
        dayButtonArray.add(frag!!.findViewById(R.id.select_monday))
        dayButtonArray.add(frag!!.findViewById(R.id.select_tuesday))
        dayButtonArray.add(frag!!.findViewById(R.id.select_wednesday))
        dayButtonArray.add(frag!!.findViewById(R.id.select_thursday))
        dayButtonArray.add(frag!!.findViewById(R.id.select_friday))
        dayButtonArray.add(frag!!.findViewById(R.id.select_saturday))
        dayButtonArray.add(frag!!.findViewById(R.id.select_sunday))


        fragCom?.days?.observe(this,
            Observer<Any> {
                    o -> fetchData(o.toString())
                Log.d(TAG, "Fetching String in onCreateView: ${returnStrings.toString()}")

            })

        for (button in dayButtonArray){
            button.background.setColorFilter(Color.parseColor(notSelColour), PorterDuff.Mode.SCREEN)
            button.setOnClickListener { buttonToggle(button) }
        }

        return frag
    }

    override fun onResume() {
        super.onResume()

        fragCom?.days?.observe(this,
            Observer<Any> {
                    o -> fetchData(o.toString())
                Log.d(TAG, "Fetching String in onResume: ${returnStrings.toString()}")

            })

    }

    override fun onPause() {
        super.onPause()

        Log.d(TAG, "in onPause: ${returnStrings.toString()}")
        if (returnStrings!!.size == 0){
            returnStrings!!.add("Select Days")
        }
        fragCom!!.setDays(Converters.fromList(returnStrings!!))

    }

    private fun fetchData(o: String){

        returnStrings = Converters.toList(o)

        for (string in returnStrings!!){
            //Log.d(TAG, string)
            when(string){
                "Monday" -> monBool = true
                "Tuesday" -> tueBool = true
                "Wednesday" -> wenBool = true
                "Thursday" -> thuBool = true
                "Friday" -> friBool = true
                "Saturday" -> satBool = true
                "Sunday" -> sunBool = true
                else ->{
                    Log.d(TAG, "Error")
                }
            }

            when(string){
                "Monday" -> dayButtonArray[0].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                "Tuesday" -> dayButtonArray[1].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                "Wednesday" -> dayButtonArray[2].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                "Thursday" -> dayButtonArray[3].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                "Friday" -> dayButtonArray[4].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                "Saturday" -> dayButtonArray[5].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                "Sunday" -> dayButtonArray[6].background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
                else ->{
                    Log.d(TAG, "Error")
                }
            }
        }
    }


    private fun buttonToggle(button: Button){
        //Log.d(TAG, "In buttonToggle")

        //adds the day to an array that is to return to the previous fragment
        when(button.id){
            dayButtonArray[0].id -> addToArray("Monday", monBool)
            dayButtonArray[1].id -> addToArray("Tuesday", tueBool)
            dayButtonArray[2].id -> addToArray("Wednesday", wenBool)
            dayButtonArray[3].id -> addToArray("Thursday", thuBool)
            dayButtonArray[4].id -> addToArray("Friday", friBool)
            dayButtonArray[5].id -> addToArray("Saturday", satBool)
            dayButtonArray[6].id -> addToArray("Sunday", sunBool)
            else ->{
                Log.d(TAG, "Error")
            }
        }

        //Changes the background colour of the button when it´s toggled
        when(button.id){
            dayButtonArray[0].id -> toggling(button, monBool)
            dayButtonArray[1].id -> toggling(button, tueBool)
            dayButtonArray[2].id -> toggling(button, wenBool)
            dayButtonArray[3].id -> toggling(button, thuBool)
            dayButtonArray[4].id -> toggling(button, friBool)
            dayButtonArray[5].id -> toggling(button, satBool)
            dayButtonArray[6].id -> toggling(button, sunBool)
            else -> {
                Log.d(TAG, "Error: change colour")
        }}

    }

    private fun addToArray(day: String, boolean: Boolean){
        //Log.d(TAG, "In addToArray")

        //Log.d(TAG, "Array size " + returnStrings.size)
        if(!boolean) {
            returnStrings!!.add(day)
        }

        for (string in returnStrings!!.indices){
            if (boolean){
                //Log.d(TAG, "Being removed " + returnStrings[string])
                returnStrings!!.removeAt(string)
                break
            }
        }

        //Log.d(TAG, "Found $returnStrings")
    }

    private fun toggling(button: Button, isSelected: Boolean){
        if (!isSelected){
            button.background.setColorFilter(Color.parseColor(selColour), PorterDuff.Mode.SCREEN)
        }else{
            button.background.setColorFilter(Color.parseColor(notSelColour), PorterDuff.Mode.SCREEN)
        }

        //Changes the bool that keeps track of weather the button is actually toggled
        when(button.id){
            dayButtonArray[0].id -> monBool = checkBool(monBool)
            dayButtonArray[1].id -> tueBool = checkBool(tueBool)
            dayButtonArray[2].id -> wenBool = checkBool(wenBool)
            dayButtonArray[3].id -> thuBool = checkBool(thuBool)
            dayButtonArray[4].id -> friBool = checkBool(friBool)
            dayButtonArray[5].id -> satBool = checkBool(satBool)
            dayButtonArray[6].id -> sunBool = checkBool(sunBool)
            else -> {
                Log.d(TAG, "Error: day boolean")
            }}
    }

    private fun checkBool (boolean: Boolean):Boolean{
        return !boolean
    }


}
