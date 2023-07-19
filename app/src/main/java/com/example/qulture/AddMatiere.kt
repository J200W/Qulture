package com.example.qulture

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class AddMatiere : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_matiere)
        val inputMatiere = findViewById<TextInputLayout>(R.id.NomMatiereAdd)
        val btn_AddMatiere : Button = findViewById(R.id.ButtonAddMatiere)
        btn_AddMatiere.setOnClickListener{
            if (inputMatiere.editText?.text.toString() != ""){
                inputMatiere.isEnabled = false
                sendMatiere()
            }
        }
    }

    private fun sendMatiere() {
        val inputMatiere = findViewById<TextInputLayout>(R.id.NomMatiereAdd)
        val db = DbFireBase()
        val MatiereName = inputMatiere.editText?.text.toString()
        inputMatiere.isEnabled = true
        val intent = Intent(this, AddQuestion::class.java)
        intent.putExtra("name_subject", MatiereName)
        startActivity(intent)
        db.updateMatiere()
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Quiz")
        startActivity(intent)
    }
}