package com.example.qulture

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qulture.DbFireBase.Singleton.downloadURI

@Suppress("DEPRECATION")
class AddFiche: AppCompatActivity() {
    private var selectedPDF: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_add_fiche)
        var btn_filePicker : Button = findViewById(R.id.searchPathBtn)
        var btn_fileAdd : Button = findViewById(R.id.addFileBtn)

        btn_filePicker.setOnClickListener{
            pickUpPDF()
        }

        btn_fileAdd.setOnClickListener{
            var text_pathShow : TextView = findViewById(R.id.pathEntry)
            var ficheUrl: TextView = findViewById(R.id.NameFileEntry)
            if (text_pathShow.text.toString() != "" && ficheUrl.text.toString() != ""){
                text_pathShow.isEnabled = false
                ficheUrl.isEnabled = false
                sendFiche(ficheUrl.text.toString())
            }
        }
    }

    private fun sendFiche(fileName : String){
        val db = DbFireBase()
        var text_pathShow : TextView = findViewById(R.id.pathEntry)
        selectedPDF = Uri.parse(text_pathShow.text.toString())
        db.uploadPDF(selectedPDF!!, fileName, Constant.user){
            var ficheUrl: TextView = findViewById(R.id.NameFileEntry)
            val ficheName = ficheUrl.text.toString()
            val newFiche = FicheModel(
                ficheName,
                downloadURI.toString()
            )

            db.insertFiche(newFiche)
            db.updateFiche()
            val message = Toast.makeText(applicationContext, "Fiche Ajouté", Toast.LENGTH_SHORT)
            message.show()
            text_pathShow.isEnabled = true
            ficheUrl.isEnabled = true
        }
    }

    private fun pickUpPDF(){
        val intent = Intent()
        intent.type = "application/pdf"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), 47)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var text_pathShow : TextView = findViewById(R.id.pathEntry)

        if (requestCode == 47 && resultCode == Activity.RESULT_OK){

            if (data == null || data.data == null) return
            val selectedPDF = data.data
            text_pathShow.text = selectedPDF.toString()
        }
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(0, 0)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("oldFrag", "Fiche")
        startActivity(intent)
    }
}