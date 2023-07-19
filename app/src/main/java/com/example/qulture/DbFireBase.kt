package com.example.qulture

import android.net.Uri
import com.example.qulture.DbFireBase.Singleton.database
import com.example.qulture.DbFireBase.Singleton.databaseFiche
import com.example.qulture.DbFireBase.Singleton.databaseMatiere
import com.example.qulture.DbFireBase.Singleton.downloadURI
import com.example.qulture.DbFireBase.Singleton.ficheList
import com.example.qulture.DbFireBase.Singleton.matiereList
import com.example.qulture.DbFireBase.Singleton.questionList
import com.example.qulture.DbFireBase.Singleton.storageRef
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class DbFireBase {

    private val BUCKET_URL = "gs://qulture-fe642.appspot.com"

    object Singleton {
        val BUCKET_URL = "gs://qulture-fe642.appspot.com"
        var database = FirebaseDatabase.getInstance().getReference(Constant.user)
        var databaseFiche = FirebaseDatabase.getInstance().getReference(Constant.user).child("Fiche").ref
        var databaseMatiere = FirebaseDatabase.getInstance().getReference(Constant.user).child("Matiere").ref
        var ficheList = arrayListOf<FicheModel>()
        var matiereList = arrayListOf<QuizModel>()
        var questionList =  mutableMapOf<String, QuestionModel>()
        var storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        var downloadURI: Uri? = null
    }

    fun updateFiche(){
        databaseFiche = FirebaseDatabase.getInstance().getReference(Constant.user).child("Fiche")
        databaseFiche.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ficheList.clear()
                for (ds in snapshot.children){

                    var ficheName = ds.child("name").value.toString()
                    var ficheNamePDF = ds.child("namePDF").value.toString()

                    ficheList.add(FicheModel(ficheName, ficheNamePDF))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun updateMatiere(){
        updateAllDatabases(Constant.user)
        FirebaseDatabase.getInstance().getReference(Constant.user).child("Matiere").ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                matiereList.clear()
                for (ds in snapshot.children){
                    val matiereName = ds.child("Nom").value.toString()
                    matiereList.add(QuizModel(matiereName))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun updateQuestion(quiz: String){
        databaseMatiere.child(quiz).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                for (ds in snapshot.children){ val question = ds.child("Question").value.toString()
                    val rep1 = ds.child("Propositions").child("BR").value.toString()
                    val rep2 = ds.child("Propositions").child("MR1").value.toString()
                    val rep3 = ds.child("Propositions").child("MR2").value.toString()

                    questionList[quiz] = (QuestionModel(question, rep1, rep2, rep3))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getQuestion(nomQuiz : String, query: Query){
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    Constant.question = ds.child("Question").value.toString()                 }
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun updateFiche(fiche : FicheModel){
        databaseFiche.child(fiche.name).setValue(fiche)
    }

    fun updateAccount(user : String){
        database = FirebaseDatabase.getInstance().getReference(user)
    }

    fun insertUser(nameU : String, passW : String){
        FirebaseDatabase.getInstance().getReference(nameU).child("MDP").setValue(passW)
        FirebaseDatabase.getInstance().getReference(nameU).child("Username").setValue(nameU)
    }

    fun checkUser(nameU : String, passW : String) : Boolean{
        var thePass = ""
        return thePass == passW
    }

    fun insertFiche(fiche : FicheModel){
        databaseFiche.child(fiche.name).setValue(fiche)
    }

    fun insertQuiz(quizModel: QuizModel){
        val key = ArrayList<String>()
        key.add("BR")
        key.add("MR1")
        key.add("MR2")

        updateAllDatabases(Constant.user)
        databaseMatiere = FirebaseDatabase.getInstance().getReference(Constant.user).child("Matiere")

        databaseMatiere.child(quizModel.name_subject)
            .child("Propositions").child(key[0]).setValue("")
        databaseMatiere.child(quizModel.name_subject)
            .child("Propositions").child(key[1]).setValue("")
        databaseMatiere.child(quizModel.name_subject)
            .child("Propositions").child(key[2]).setValue("")
        databaseMatiere.child(quizModel.name_subject)
            .child("Question").setValue("")
        databaseMatiere.child(quizModel.name_subject)
            .child("Nom").setValue("")
    }

    fun insertQuestion(questionModel: QuestionModel, name_sub: String){
        val key = ArrayList<String>()
        key.add("BR")
        key.add("MR1")
        key.add("MR2")
        val rep = ArrayList<String>()
        rep.add(questionModel.BR)
        rep.add(questionModel.MR1)
        rep.add(questionModel.MR2)

        updateAllDatabases(Constant.user)
        databaseMatiere = FirebaseDatabase.getInstance().getReference(Constant.user).child("Matiere")

        databaseMatiere.child(name_sub)
            .child("Propositions").child(key[0]).setValue(rep[0])
        databaseMatiere.child(name_sub)
            .child("Propositions").child(key[1]).setValue(rep[1])
        databaseMatiere.child(name_sub)
            .child("Propositions").child(key[2]).setValue(rep[2])
        databaseMatiere.child(name_sub)
            .child("Question").setValue(questionModel.Question)
        databaseMatiere.child(name_sub)
            .child("Nom").setValue(name_sub)

    }

    fun updateAllDatabases(user : String){
        databaseFiche = FirebaseDatabase.getInstance().getReference(user).child("Fiche")
        databaseMatiere = FirebaseDatabase.getInstance().getReference(user).child("Matiere")
    }

    fun uploadPDF(file : Uri, fileName : String, user: String, callback: ()->Unit){
        if (file != null){
            val fileName = fileName+"__"+user+".pdf"
            val ref = storageRef.child(fileName)
            val uploadTask = ref.putFile(file)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful){
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    downloadURI = task.result
                    callback()
                }
            }
        }
    }
    fun deletePDF(name : String){
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(Singleton.BUCKET_URL)
        storageRef.child(name+"__"+Constant.user+".pdf").delete().addOnSuccessListener {
            print("\n\n\nDeleting : $name")
        }
    }
}