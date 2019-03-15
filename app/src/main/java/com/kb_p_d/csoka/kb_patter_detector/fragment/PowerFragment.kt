package com.kb_p_d.csoka.kb_patter_detector.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import com.kb_p_d.csoka.kb_patter_detector.R
import com.kb_p_d.csoka.kb_patter_detector.service.HUDService

class PowerFragment : Fragment() {
    lateinit var currentView: View
    lateinit var switch: Switch

    //TODO - a lot - use HUDService to register gestures and save each gesture (when finger is lifted from the screen) automatically to kb-pat-det/captured

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_power, null)
        switch = currentView.findViewById(R.id.switchPower)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Log.d("PowerFrag", "isChecked = true")
                switch.isChecked = true
                activity!!.startService(Intent(activity, HUDService::class.java))
            } else {
                Log.d("PowerFrag", "isChecked = false")
                switch.isChecked = false
            }
        }

        return currentView
    }
}