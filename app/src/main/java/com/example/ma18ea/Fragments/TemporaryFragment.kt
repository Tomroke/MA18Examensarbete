package com.example.ma18ea.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.ma18ea.R

private const val ARG_PARAM1 = "param1"

class TemporaryFragment : Fragment() {
    private var param1: String? = "temp"

    companion object {
        @JvmStatic
        fun newInstance(string: String) =
            TemporaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, string)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temporary, container, false)
    }

}
