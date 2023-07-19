package com.example.qulture

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

class ExitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exit)
        Handler(Looper.getMainLooper()).postDelayed(
            { exitProcess(0) }, 3000)
    }
}