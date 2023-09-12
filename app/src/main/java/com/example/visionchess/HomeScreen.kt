package com.example.visionchess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

    }
//    override fun onBackPressed() {
//        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
//
//        if (currentFragment is SettingsFragment) {
//            // Remove the Runnable callbacks when navigating back from SettingsFragment
//            currentFragment.removeCallbacksAndMessages()
//        }
//
//        super.onBackPressed() // Continue with the default back navigation behavior
//    }
}
