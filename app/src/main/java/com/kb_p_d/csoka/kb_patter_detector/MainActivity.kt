package com.kb_p_d.csoka.kb_patter_detector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.kb_p_d.csoka.kb_patter_detector.fragment.HomeFragment
import com.kb_p_d.csoka.kb_patter_detector.fragment.LibraryFragment
import com.kb_p_d.csoka.kb_patter_detector.fragment.PowerFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var fragment : Fragment = HomeFragment()
        when (item.itemId) {
            R.id.navigation_home -> {
                //startActivity(Intent(this@MainActivity, MainActivity::class.java))
                //return@OnNavigationItemSelectedListener true
                fragment = HomeFragment()
            }
            R.id.navigation_lib -> {
                //startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
                //return@OnNavigationItemSelectedListener true
                fragment = LibraryFragment()
            }
            R.id.navigation_power -> {
                //startActivity(Intent(this@MainActivity, PowerActivity::class.java))
                //return@OnNavigationItemSelectedListener true
                fragment = PowerFragment()
            }
        }
        //false
        loadFragment(fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(HomeFragment())
    }



    fun loadFragment(fragment: Fragment) : Boolean {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frag_container, fragment)
            .commit()
        return true
    }
}
