package com.example.qulture.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.qulture.*
import com.example.qulture.adapter.QuizAdapter

class MesQuizFragment(
    private val context :MainActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.mes_quiz_fragment, container, false)
        val db = DbFireBase()
        db.updateAllDatabases(Constant.user)

        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.mes_quiz_recycler_view_list)
        verticalRecyclerView.adapter = QuizAdapter(context, DbFireBase.Singleton.matiereList, R.layout.item_vertical_quiz)

        val addQuiz : TextView? = view.findViewById<Button>(R.id.addQuizBtn)
        addQuiz?.setOnClickListener {
            val intent = Intent(context, AddMatiere::class.java)
            context.startActivity(intent)
        }

        // Manipulate the Recycler view
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}