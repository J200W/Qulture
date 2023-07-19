package com.example.qulture

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.qulture.fragments.MesFichesFragment
import com.example.qulture.fragments.MesQuizFragment
import com.example.qulture.fragments.MonCompteConnected
import com.example.qulture.fragments.MonCompteFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    val ficheFrag = MesFichesFragment(this)
    val quizFrag = MesQuizFragment(this)
    val noCompteFrag = MonCompteFragment(this)
    val CompteFrag = MonCompteConnected(this)
    var ficheRef = FirebaseDatabase.getInstance().getReference(Constant.user).child("Fiche")
    var quizRef = FirebaseDatabase.getInstance().getReference(Constant.user).child("Matiere")

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        super.onCreate(savedInstanceState)
        val oldFrag = intent.extras?.get("oldFrag").toString()

        val db = DbFireBase()
        if (!Constant.log){
            db.insertUser(Constant.user, "12345")
        }
        db.updateAllDatabases(Constant.user)
        db.updateMatiere()
        db.updateFiche()

        setContentView(R.layout.activity_main)
        if (!Constant.log){
            if (oldFrag == "Compte"){
                replaceFragment(noCompteFrag)
                findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_compte

            }
            if (oldFrag == "Fiche"){
                replaceFragment(ficheFrag)
                findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_file

            }
            if (oldFrag == "Quiz"){
                replaceFragment(quizFrag)
                findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_quiz

            }
        }
        else {
            Constant.quizSize = 0
            Constant.ficheSize = 0

            ficheRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        Constant.ficheSize += 1
                    }
                }
                override fun onCancelled(error: DatabaseError){}
            })

            quizRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        Constant.quizSize += 1
                    }
                }
                override fun onCancelled(error: DatabaseError){}
            })
            Handler(Looper.getMainLooper()).postDelayed({
                if (oldFrag == "Compte"){
                    replaceFragment(CompteFrag)
                    findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_compte

                }
                if (oldFrag == "Fiche"){
                    replaceFragment(ficheFrag)
                    findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_file

                }
                if (oldFrag == "Quiz"){
                    replaceFragment(quizFrag)
                    findViewById<BottomNavigationView>(R.id.bottom_navigation).selectedItemId = R.id.ic_quiz

                }
            }, 200)
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener{
            when (it.itemId){
                R.id.ic_file -> {replaceFragment(ficheFrag)}
                R.id.ic_compte -> {
                    if (!Constant.log){
                        replaceFragment(noCompteFrag)
                    }
                    else {
                        replaceFragment(CompteFrag)
                    }
                }
                R.id.ic_quiz -> replaceFragment(quizFrag)
            }
            true
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).apply {
            setTitle("Veuillez confirmer.")
            setMessage("Voulez-vous quitter Qulture ?")

            setPositiveButton("Oui") { _, _ ->
                // if user press yes, then finish the current activity
                this@MainActivity.finish()
                val intent = Intent(context, ExitActivity::class.java)
                context.startActivity(intent)
            }

            setNegativeButton("Non"){_, _ ->
                // if user press no, then return the activity
                Toast.makeText(this@MainActivity, "Merci",
                    Toast.LENGTH_LONG).show()
            }

            setCancelable(true)
        }.create().show()
    }

    private fun replaceFragment(fragment: Fragment){
        val db = DbFireBase()
        if (fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
            if (fragment == MesFichesFragment(this)){
                db.updateFiche()
            }
            else{
                db.updateMatiere()
            }
        }
    }
}