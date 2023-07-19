package com.example.qulture.adapter

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.qulture.*
import com.example.qulture.DbFireBase.Singleton.databaseMatiere
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class QuizAdapter(
    private val context: MainActivity,
    private val quizList: List<QuizModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<QuizAdapter.ViewHolder>() {
    val db = DbFireBase()

    private val EXTRA_NAME_QUIZ = "com.example.application.qulture.EXTRA_NAME_QUIZ"

    // Box to arrange all the controlled components
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        // matiere image
        val quizImage = view.findViewById<ImageView>(R.id.image_item_quiz)
        val quizName:TextView? = view.findViewById<Button>(R.id.button_item_quiz)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the information of a subject
        val currentMatiere = quizList[position]
        val resourceId = R.drawable.qulture_logo_quiz

        holder.quizImage.setImageResource(resourceId)

        holder.quizName?.text = currentMatiere.name_subject
        holder.quizName?.setOnClickListener{
            databaseMatiere.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    DbFireBase.Singleton.ficheList.clear()
                    var i = 0
                    for (ds in snapshot.children){
                        i+=1
                        Constant.numberQuestion = i
                    }
                }
                override fun onCancelled(error: DatabaseError) {}

            })
            Constant.nameQuiz = holder.quizName.text.toString()

            val intent = Intent(context, ReadData::class.java)
            intent.putExtra("nomQuiz", Constant.nameQuiz)
            context.startActivity(intent)
        }


        holder.quizImage?.setOnClickListener{

            AlertDialog.Builder(context).apply {
                setTitle("Veuillez confirmer.")
                setMessage("Voulez-vous supprimer ce quiz ?")

                setPositiveButton("Oui") { _, _ ->
                    // if user press yes, then finish the current activity
                    databaseMatiere.child(currentMatiere.name_subject).removeValue()
                    db.updateMatiere()

                    val intent = Intent(context, Supression::class.java)
                    intent.putExtra("oldFrag", "Quiz")
                    context.startActivity(intent)

                }

                setNegativeButton("Non"){_, _ ->
                    // if user press no, then return the activity
                    Toast.makeText(context, "Ok !", Toast.LENGTH_LONG).show()
                }

                setCancelable(true)
            }.create().show()
        }
    }

    override fun getItemCount(): Int = quizList.size

}