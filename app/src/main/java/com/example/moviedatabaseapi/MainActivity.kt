package com.example.moviedatabaseapi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Denna pekar på layouten som innehåller FragmentContainerView
        setContentView(R.layout.activity_main)
    }
} //test