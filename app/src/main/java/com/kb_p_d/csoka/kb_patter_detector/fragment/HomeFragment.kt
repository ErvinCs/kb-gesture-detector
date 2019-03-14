package com.kb_p_d.csoka.kb_patter_detector.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.gesture.GestureOverlayView
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.kb_p_d.csoka.kb_patter_detector.R
import com.kb_p_d.csoka.kb_patter_detector.service.GestureListenerImpl
import com.kb_p_d.csoka.kb_patter_detector.MainActivity
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import android.graphics.Bitmap
import android.os.Environment
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.support.annotation.NonNull
import android.text.InputType
import android.widget.EditText
import com.kb_p_d.csoka.kb_patter_detector.Code
import java.io.File
import java.io.FileOutputStream

//TODO - pattern naming scheme
class HomeFragment : Fragment() {
    lateinit var currentView: View
    lateinit var gestureOverlayView: GestureOverlayView
    lateinit var buttonSave: Button
    lateinit var buttonDiscard: Button
    lateinit var etSaveName: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        currentView = inflater.inflate(R.layout.fragment_home, null)

        gestureOverlayView = currentView.findViewById(R.id.paint_pad)
        buttonSave = currentView.findViewById(R.id.btnSave)
        buttonDiscard = currentView.findViewById(R.id.btnDiscard)
        etSaveName = currentView.findViewById(R.id.etSaveName)

        gestureOverlayView.addOnGesturePerformedListener(GestureListenerImpl())
        buttonSave.setOnClickListener{ checkPermissions() }
        buttonDiscard.setOnClickListener{ gestureOverlayView.clear(false) }

        return currentView
    }

    private fun savePattern() {
        try {
            // First destroy cached image.
            gestureOverlayView.destroyDrawingCache()

            // Enable drawing cache function.
            gestureOverlayView.isDrawingCacheEnabled = true

            // Get drawing cache bitmap.
            val drawingCacheBitmap = gestureOverlayView.drawingCache

            // Create a new bitmap
            val bitmap = Bitmap.createBitmap(drawingCacheBitmap)

            // Get image file save path and name.
            var filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "DCIM" + File.separator + Code.STORAGE_PATH.key
            val folder = File(filePath)
            if(!File(filePath).exists())
                folder.mkdirs()
            filePath += File.separator + etSaveName.text.toString().trim() + ".png"

            val file = File(filePath)
            file.createNewFile()
            val fileOutputStream = FileOutputStream(file)

            // Compress bitmap to png image.
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)

            // Flush bitmap to image file.
            fileOutputStream.flush()

            // Close the output stream.
            fileOutputStream.close()

            Toast.makeText(super.requireContext(), "Signature file is saved to $filePath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.v("Signature Gestures", e.message)
            e.printStackTrace()
        }
    }

    private fun checkPermissions() {
        try {
            // Check whether this app has write external storage permission or not.
            var writeExternal = ContextCompat.checkSelfPermission(super.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

            // If do not grant write external storage permission.
            if (writeExternal != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(super.requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    Code.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION.value
                )
            } else {
                savePattern()
            }
        } catch (e: Exception) {
            Log.v("Signature Gestures", e.message)
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Code.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION.value) {
            val grantResultsLength = grantResults.size
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                savePattern()
            } else {
                Toast.makeText(super.requireContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show()
            }
        }
    }
}