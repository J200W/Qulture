package com.example.qulture.adapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.qulture.*
import com.example.qulture.DbFireBase.Singleton.BUCKET_URL
import com.example.qulture.DbFireBase.Singleton.databaseFiche
import com.example.qulture.DbFireBase.Singleton.storageRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class FicheAdapter(
    val context: MainActivity,
    private val FicheList: List<FicheModel>,
    private val layoutId: Int
)  : RecyclerView.Adapter<FicheAdapter.ViewHolder>(){
    val db = DbFireBase()


    // Box to arrange all the controlled components
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        // fiche image
        val ficheImage = view.findViewById<ImageView>(R.id.image_item_fiche)
        val ficheName: TextView? = view.findViewById<Button>(R.id.button_item_fiche)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        // Get the information of a subject
        val currentFiche = FicheList[position]
        val resourceId = R.drawable.ic_file
        holder.ficheImage.setImageResource(resourceId)

        holder.ficheName?.text = currentFiche.name

        holder.ficheName?.setOnClickListener{
            Constant.name = holder.ficheName.text.toString()
            databaseFiche.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    DbFireBase.Singleton.ficheList.clear()
                    val i : Int = 0
                    for (ds in snapshot.children){
                        var ficheName = ds.child("name").value.toString()
                        var ficheNamePDF = ds.child("namePDF").value.toString()

                        if (Constant.name == ficheName){
                            Constant.local = ds.child("local").value.toString()
                            break
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {}

            })
            val intent = Intent(context, FicheActivity::class.java)
            context.startActivity(intent)
        }
        holder.ficheImage?.setOnClickListener{

            AlertDialog.Builder(context).apply {
                setTitle("Veuillez confirmer.")
                setMessage("Voulez-vous supprimer la fiche ?")

                setPositiveButton("Oui") { _, _ ->
                    // if user press yes, then finish the current activity
                    Handler(Looper.getMainLooper()).postDelayed({
                        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
                        Handler(Looper.getMainLooper()).postDelayed({

                            storageRef.child(currentFiche.name+"__"+Constant.user+".pdf").delete().addOnSuccessListener {
                                databaseFiche.child(currentFiche.name).removeValue()
                                val intent = Intent(context, Supression::class.java)
                                intent.putExtra("oldFrag", "Fiche")
                                context.startActivity(intent)
                            }.addOnFailureListener {
                                Toast.makeText(context, "Erreur", Toast.LENGTH_LONG).show()
                            }
                        },200)
                    }, 200)
                }

                setNegativeButton("Non"){_, _ ->
                    // if user press no, then return the activity
                    Toast.makeText(context, "Ok !", Toast.LENGTH_LONG).show()
                }

                setCancelable(true)
            }.create().show()
        }
    }

    override fun getItemCount(): Int = FicheList.size
}