package com.example.qulture

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class Supression : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supression)
        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 1000)
    }

    override fun onBackPressed() {
        val oldFrag = intent.extras?.get("oldFrag").toString()
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        if (oldFrag == "Fiche") intent.putExtra("oldFrag", "Fiche")
        if (oldFrag == "Quiz") intent.putExtra("oldFrag", "Quiz")
        if (oldFrag == "Compte") intent.putExtra("oldFrag", "Compte")
        startActivity(intent)
    }
}