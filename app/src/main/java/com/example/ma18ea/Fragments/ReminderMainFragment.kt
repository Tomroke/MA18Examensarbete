package com.example.ma18ea.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.ma18ea.R
import com.example.ma18ea.ReminderVariables
import kotlinx.android.synthetic.main.fragment_reminder_main.*
import kotlinx.android.synthetic.main.fragment_reminder_main.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"

class ReminderMainFragment : Fragment() {

    private val TAG : String = "RMFragment"

    private var root: View? = null

    private var param1: String? = null
    private var param2: String? = null
    private var param3: Int? = null

    //private var titleText : TextView? = null

    //private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, " In onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            param3 = it.getInt(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, " In onCreateView")
        root = inflater.inflate(R.layout.fragment_reminder_main, container, false)
        root!!.title_reminder_txt.text = param1
        root!!.description_txt.setText(param2)
        root!!.progressbar_in_reminder.progress = this.param3!!
        return root
    }

    /*// TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }*/

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }*/

    /*override fun onDetach() {
        super.onDetach()
        listener = null
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }*/

    //Takes in a variable or object and sets it in a bundle(?)
    companion object {
        @JvmStatic
        fun newInstance(fragAR : ReminderVariables) =
            ReminderMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, fragAR.title)
                    putString(ARG_PARAM2, fragAR.time)
                    putInt(ARG_PARAM3, fragAR.barProgress)
                }
            }
    }
}
