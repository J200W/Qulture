package com.example.qulture

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class DeconnexionActivity : AppCompatActivity() {
    val db = DbFireBase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deconnexion)
        Constant.log = false
        Constant.user = "INVITE"
        db.updateAllDatabases(Constant.user)
        db.updateAccount(Constant.user)
        Handler(Looper.getMainLooper()).postDelayed({
            onBackPressed()
        }, 1000)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Compte")
        startActivity(intent)
    }
}