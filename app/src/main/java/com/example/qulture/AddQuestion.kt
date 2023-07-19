package com.example.qulture

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddQuestion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        val quest = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.quest)
        val goodAnsw = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.goodAnsw)
        val badAns1 = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.badAns1)
        val badAns2 = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.badAns2)
        val quizConfirmQuizzBtn = findViewById<Button>(R.id.quizConfirmQuizzBtn)
        val name_sub = intent.extras?.get("name_subject").toString()

        quizConfirmQuizzBtn.setOnClickListener {
            if (quest.text.toString() != "" && goodAnsw.text.toString() != ""
                && badAns1.text.toString() != "" && badAns2.text.toString() != "") {
                val questionModel = QuestionModel(quest.text.toString(), goodAnsw.text.toString(),
                    badAns1.text.toString(), badAns2.text.toString())
                val db = DbFireBase()
                db.insertQuestion(questionModel, name_sub)
                val message = Toast.makeText(applicationContext, "Quiz Ajouté", Toast.LENGTH_SHORT)
                message.show()
                onBackPressed()
            }
            else {
                var ques = "Question"
                var br = "BonneRep"
                var mr1 = "MauvRep1"
                var mr2 = "MauvRep2"

                if (quest.text.toString() != ""){
                    ques = quest.text.toString()
                }
                if (goodAnsw.text.toString() != ""){
                    br = goodAnsw.text.toString()
                }
                if (badAns1.text.toString() != ""){
                    mr1 = badAns1.text.toString()
                }
                if (badAns2.text.toString() != ""){
                    mr2 = badAns1.text.toString()
                }
                val questionModel = QuestionModel(ques, br, mr1, mr2)
                val db = DbFireBase()
                db.insertQuestion(questionModel, name_sub)
                val message = Toast.makeText(applicationContext, "Quiz Ajouté", Toast.LENGTH_SHORT)
                message.show()
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Quiz")
        startActivity(intent)
    }

}