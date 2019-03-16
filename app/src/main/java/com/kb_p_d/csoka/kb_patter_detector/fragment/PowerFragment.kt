package com.kb_p_d.csoka.kb_patter_detector.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.gesture.GestureOverlayView
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.Toast
import com.kb_p_d.csoka.kb_patter_detector.Code
import com.kb_p_d.csoka.kb_patter_detector.R
import com.kb_p_d.csoka.kb_patter_detector.service.HUD2Service
import com.kb_p_d.csoka.kb_patter_detector.service.HUDService

//TODO - app bar on top while the service/app is running in the background (soundcloud-like)
//TODO - save captured patterns with name: DD-MM-YYY_HH-MM-SS
class PowerFragment : Fragment() {
    lateinit var currentView: View
    lateinit var switch: Switch
    lateinit var gestureOverlayView: GestureOverlayView

    //TODO - a lot - use HUDService to register gestures and save each gesture (when finger is lifted from the screen) automatically to kb-pat-det/captured

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_power, null)
        switch = currentView.findViewById(R.id.switchPower)
        gestureOverlayView = currentView.findViewById(R.id.paint_pad_power)

        checkPermissions()

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.d("PowerFrag", "isChecked = true")
                switch.isChecked = true
                activity!!.startService(Intent(activity, HUD2Service::class.java))
            } else {
                Log.d("PowerFrag", "isChecked = false")
                switch.isChecked = false
                activity!!.stopService(Intent(activity, HUD2Service::class.java))
            }
        }

        return currentView
    }

    private fun checkPermissions() {
        try {
            // Check whether this app has write external storage permission or not.
            var sysAlert = ContextCompat.checkSelfPermission(super.requireContext(), Manifest.permission.SYSTEM_ALERT_WINDOW)

            // If do not grant write external storage permission.
            if (sysAlert != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(
                    super.requireActivity(),
                    arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW),
                    Code.REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION.value
                )
            }
        } catch (e: Exception) {
            Log.v("Signature Gestures", e.message)
            e.printStackTrace()
        }

        if (!Settings.canDrawOverlays(super.requireContext())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + activity!!.packageName)
            )
            startActivityForResult(intent, 1234)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Code.REQUEST_CODE_SYSTEM_ALERT_WINDOW_PERMISSION.value) {
            val grantResultsLength = grantResults.size
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //activity!!.startService(Intent(activity, HUD2Service::class.java))
                switch.isChecked = true
            } else {
                Toast.makeText(super.requireContext(), "You denied permission.", Toast.LENGTH_LONG).show()
            }
        }

    }
}