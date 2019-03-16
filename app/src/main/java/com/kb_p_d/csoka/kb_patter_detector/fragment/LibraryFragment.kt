package com.kb_p_d.csoka.kb_patter_detector.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kb_p_d.csoka.kb_patter_detector.Code
import com.kb_p_d.csoka.kb_patter_detector.R

//TODO - List of files from: Environment.getExternalStorageDirectory().toString() + File.separator + storage_path
//TODO - Separate folders in the app folder for: drawn patterns, captured patterns (these will be automatically captured)
//TODO - Separate access from the library to draw and to captured (maybe)
class LibraryFragment : Fragment() {
    lateinit var currentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_library, null)

        checkPermissions()

        return currentView
    }

    private fun checkPermissions() {
        try {
            // Check whether this app has write external storage permission or not.
            var readExternal = ContextCompat.checkSelfPermission(super.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)

            // If do not grant write external storage permission.
            if (readExternal != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(super.requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Code.REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION.value
                )
            }
        } catch (e: Exception) {
            Log.v("Signature Gestures", e.message)
            e.printStackTrace()
        }
    }
}