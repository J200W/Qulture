package com.example.qulture

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qulture.databinding.ActivityQuizBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ReadData : AppCompatActivity() {
    private lateinit var binding : ActivityQuizBinding
    private lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extra =intent.extras
        var namequiz = extra?.getString("nomQuiz")
        if (namequiz != null) {
            readData(namequiz)
        }
    }
    private fun readData(namequiz : String){
        database = FirebaseDatabase.getInstance().getReference(Constant.user).child("Matiere")
        database.child(namequiz).get().addOnSuccessListener {
            if (it.exists()){

                val question = it.child("Question").value
                val rep1 = it.child("Propositions").child("BR").value
                val rep2 = it.child("Propositions").child("MR1").value
                val rep3 = it.child("Propositions").child("MR2").value
                binding.TheQuestion.text = question.toString()
                var int = (0..2).random()
                var allRep = arrayListOf<String>()
                allRep.add(rep1.toString())
                allRep.add(rep2.toString())
                allRep.add(rep3.toString())
                binding.REP1.text = allRep[int]
                allRep.removeAt(int)
                int = (0..1).random()
                binding.REP2.text = allRep[int]
                allRep.removeAt(int)
                int = 0
                binding.REP3.text = allRep[int]
                allRep.clear()

                binding.REP1.setOnClickListener {
                    if (binding.REP1.text.toString() == rep1){
                        binding.REP1.setTextColor(Color.BLACK)
                        binding.REP1.setBackgroundColor(Color.GREEN)
                        Toast.makeText(this,"Bonne réponse !",Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            onBackPressed()
                        }, 1500)
                    }
                    else {
                        binding.REP1.setBackgroundColor(Color.RED)
                        Toast.makeText(this,"Mauvaise réponse !",Toast.LENGTH_SHORT).show()
                    }
                }
                binding.REP2.setOnClickListener {
                    if (binding.REP2.text.toString() == rep1){
                        binding.REP2.setTextColor(Color.BLACK)
                        binding.REP2.setBackgroundColor(Color.GREEN)
                        Toast.makeText(this,"Bonne réponse !",Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            onBackPressed()
                        }, 1500)
                    }
                    else {
                        binding.REP2.setBackgroundColor(Color.RED)
                        Toast.makeText(this,"Mauvaise réponse !",Toast.LENGTH_SHORT).show()
                    }
                }
                binding.REP3.setOnClickListener {
                    if (binding.REP3.text.toString() == rep1){
                        binding.REP3.setTextColor(Color.BLACK)
                        binding.REP3.setBackgroundColor(Color.GREEN)
                        Toast.makeText(this,"Bonne réponse !",Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            onBackPressed()
                        }, 1500)
                    }
                    else {
                        binding.REP3.setBackgroundColor(Color.RED)
                        Toast.makeText(this,"Mauvaise réponse !",Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                Toast.makeText(this,"Raté",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(this,"epic raté",Toast.LENGTH_SHORT).show()
        }
    }

    private fun quit() {
        overridePendingTransition(0, 100)
        finish()
        overridePendingTransition(0, 0)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Quiz")
        startActivity(intent)
    }
}