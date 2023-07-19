package com.example.qulture.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.qulture.*
import com.google.firebase.database.FirebaseDatabase

class MonCompteFragment (
    private val context : MainActivity
) : Fragment() {
    val db = DbFireBase()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.compte_fragment, container, false)
        val btnconnexion = view?.findViewById<Button>(R.id.Connexion)
        val nameText = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.NameU)
        val passWord = view?.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.PasswordAuth)
        val btnCreateAccount = view?.findViewById<Button>(R.id.CreerCompteBtn)

        btnCreateAccount?.setOnClickListener {
            val intent = Intent(context, CreateAccount::class.java)
            startActivity(intent)
        }

        btnconnexion?.setOnClickListener {
            nameText?.isEnabled = false
            passWord?.isEnabled = false
            if (nameText?.text.toString().trimEnd() != ""){
                FirebaseDatabase.getInstance().getReference(nameText?.text.toString().trimEnd().uppercase()).get().addOnSuccessListener {
                    if (it.exists()){
                        Constant.passW = it.child("MDP").value.toString()
                    }
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    if (Constant.passW == passWord?.text.toString() && Constant.passW != ""
                        && nameText?.text.toString().uppercase().trimEnd() != "INVITE"){
                        val intent = Intent(context, LoginUser::class.java)
                        intent.putExtra("nameU", nameText?.text.toString().trimEnd())
                        if (Constant.passW == passWord?.text.toString()){
                            db.updateAccount(nameText?.text.toString().uppercase().trimEnd())
                            db.updateAllDatabases(nameText?.text.toString().uppercase().trimEnd())
                            Constant.user = nameText?.text.toString().uppercase().trimEnd()
                            context.startActivity(intent)
                            nameText?.setText("")
                            passWord?.setText("")
                            Constant.log = true
                            Constant.passW = ""
                        }
                        else {
                            Toast.makeText(context,
                                "Mot de passe ou utilisateur erroné !",
                                Toast.LENGTH_LONG)
                                .show()
                            passWord?.setText("")
                            Constant.passW = ""
                        }

                    }
                    else {
                        Toast.makeText(context,
                            "Mot de passe ou utilisateur erroné !",
                            Toast.LENGTH_LONG)
                            .show()
                        passWord?.setText("")
                        Constant.passW = ""
                    }
                },2000)
            }
            else {
                Toast.makeText(context,
                    "Veuillez entrer les informations.",
                    Toast.LENGTH_LONG)
                    .show()
            }
            nameText?.isEnabled = true
            passWord?.isEnabled = true
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}