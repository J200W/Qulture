package com.example.qulture

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase


@Suppress("DEPRECATION")
class FicheActivity: AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        setContentView(R.layout.activity_fiche)
        val databaseFiche = FirebaseDatabase.getInstance().getReference("Fiche")

        if (Constant.name != ""){
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(Constant.local), "application/pdf")
            startActivity(Intent.createChooser(intent, "Choose an Application:"))
        }
        Constant.local = ""
        finish()
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Fiche")
        startActivity(intent)
    }
}