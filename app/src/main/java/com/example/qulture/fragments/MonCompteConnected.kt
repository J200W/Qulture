package com.example.qulture.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.qulture.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MonCompteConnected (
    private val context : MainActivity
) : Fragment() {

    val db = DbFireBase()

    var ficheRef = FirebaseDatabase.getInstance().getReference(Constant.user.trimEnd()).child("Fiche")
    var quizRef = FirebaseDatabase.getInstance().getReference(Constant.user.trimEnd()).child("Matiere")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.activity_mon_compte_connected, container, false)
        val Deconnexion = view?.findViewById<Button>(R.id.Deconnexion)

        val salutationXXX = view?.findViewById<TextView>(R.id.salutationXXX)
        val QuizNumb = view?.findViewById<TextView>(R.id.QuizNumb)
        val FicheNumb = view?.findViewById<TextView>(R.id.FicheNumb)
        val btnCreateAccount = view?.findViewById<Button>(R.id.CreerCompteBtn)
        val SupprimerCompte = view?.findViewById<Button>(R.id.SupprimerCompte)

        btnCreateAccount?.setOnClickListener {
            val intent = Intent(context, CreateAccount::class.java)
            startActivity(intent)
        }

        SupprimerCompte?.setOnClickListener {

            AlertDialog.Builder(context).apply {
                setTitle("Veuillez confirmer.")
                setMessage("Voulez-vous supprimer ce compte ?")
                setPositiveButton("Oui") { _, _ ->
                    Constant.log = false
                    val dataUser = FirebaseDatabase.getInstance().getReference(Constant.user)
                    dataUser.get().addOnSuccessListener {
                        if (it.exists()){
                            if (it.child("Fiche").exists()){
                                dataUser.child("Fiche").addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (ds in snapshot.children){
                                            db.deletePDF(ds.child("name").value.toString())
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                                it.child("Fiche").ref.removeValue()
                            }
                            if (it.child("MDP").exists()) it.child("MDP").ref.removeValue()
                            if (it.child("Matiere").exists()) it.child("Matiere").ref.removeValue()
                            if (it.child("Username").exists()) it.child("Username").ref.removeValue()
                        }
                    }
                    Handler(Looper.getMainLooper()).postDelayed({
                        Constant.user = "INVITE"
                        val intent = Intent(context, Supression::class.java)
                        intent.putExtra("oldFrag", "Compte")
                        startActivity(intent)
                    }, 200)
                }
                setNegativeButton("Non"){_, _ ->
                    Toast.makeText(context, "Ok !", Toast.LENGTH_LONG).show()
                }
            }.create().show()
        }
        salutationXXX?.text = "Salut "+Constant.user+ " ! Tu as dans ton compte :"
        QuizNumb?.text = "" + Constant.quizSize + " quiz"
        if (Constant.ficheSize > 1) FicheNumb?.text = ""+ Constant.ficheSize + " fiches"
        else FicheNumb?.text = ""+ Constant.ficheSize + " fiche"
        Deconnexion?.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Veuillez confirmer.")
                setMessage("Voulez-vous déconnecter ?")

                setPositiveButton("Oui") { _, _ ->
                    // if user press yes, then finish the current activity
                    val intent = Intent(context, DeconnexionActivity::class.java)
                    intent.putExtra("Username", Constant.user)
                    startActivity(intent)
                }

                setNegativeButton("Non"){_, _ ->
                    // if user press no, then return the activity
                    Toast.makeText(context, "Ok !", Toast.LENGTH_LONG).show()
                }

                setCancelable(true)
            }.create().show()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}