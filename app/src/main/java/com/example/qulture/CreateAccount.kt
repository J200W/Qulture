package com.example.qulture

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        val NewUserName = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.NewUserName)
        val PasswordCreate = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.PasswordCreate)
        val PasswordConfirm = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.PasswordConfirm)
        val CreateAccountConfirm = findViewById<Button>(R.id.CreateAccountConfirm)
        val db = DbFireBase()

        CreateAccountConfirm.setOnClickListener {
            if(NewUserName.text.toString() != ""
                && PasswordCreate.text.toString() != ""
                && PasswordConfirm.text.toString() != ""
                && PasswordCreate.text.toString() == PasswordConfirm.text.toString()){
                db.insertUser(NewUserName.text.toString().uppercase(), PasswordCreate.text.toString())
                Toast.makeText(this,
                    "Compte créée !",
                    Toast.LENGTH_LONG)
                    .show()
            }
            else {
                Toast.makeText(this,
                    "Veuillez remplir les conditions !",
                    Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Compte")
        startActivity(intent)
    }
}