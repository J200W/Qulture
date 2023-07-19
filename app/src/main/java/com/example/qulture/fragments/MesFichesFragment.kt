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
import com.example.qulture.DbFireBase.Singleton.ficheList
import com.example.qulture.adapter.FicheAdapter

class MesFichesFragment(
    private val context: MainActivity
) : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.mes_fiches_fragment, container, false)
        val db = DbFireBase()
        db.updateAllDatabases(Constant.user)
        db.updateFiche()

        // Get the recycler view of mes_fiches_fragment
        val mesFichesRecyclerView = view.findViewById<RecyclerView>(R.id.mes_fiches_recycler_view_list)
        mesFichesRecyclerView.adapter = FicheAdapter(context, ficheList, R.layout.item_vertical_fiche)
        val addFiche : TextView? = view.findViewById<Button>(R.id.AddFicheButton)
        addFiche?.setOnClickListener {
            val intent = Intent(context, AddFiche::class.java)
            context.startActivity(intent)
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}