package com.example.qulture

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)
        val nameB = findViewById<TextView>(R.id.NameB)
        nameB.text = intent.extras?.get("nameU").toString()
        val db = DbFireBase()
        db.updateAllDatabases(nameB.text.toString().uppercase())
        db.updateFiche()
        Constant.log = true
        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 2000)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Compte")
        startActivity(intent)
    }
}