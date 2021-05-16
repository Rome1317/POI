package com.example.birdline.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.birdline.R
import com.example.birdline.models.ReferenciasFirebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AssignmentActivity : AppCompatActivity() {

    val firebase  = FirebaseFirestore.getInstance();

    lateinit var points:String
    lateinit var status:String
    var uploaded:Boolean = false

    //Photo & Files
    private val fileResult = 1
    var urlImagen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment)

        // Choose File
        val getFile = findViewById<Button>(R.id.filebtn2)

        getFile.setOnClickListener {

            if(status != "Submitted"){
                FileManager()
            }
            else{
                Toast.makeText(baseContext, "You already submitted the assignment", Toast.LENGTH_LONG).show()
            }

        }


        var task_id = intent.getStringExtra("taskid")
        var group_id = intent.getStringExtra("groupid")

        var btn_submit: Button = findViewById(R.id.btn_uploadtask)
        var comments: EditText = findViewById(R.id.messageTextField2)

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(group_id.toString())

        userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).get()
            .addOnSuccessListener{
                var id = it.get("id").toString()
                var title = it.get("title").toString()
                var description = it.get("description").toString()
                points = it.get("points").toString()
                var score = it.get("score").toString()
                status = it.get("status").toString()
                var users = it.get("users").toString()
                var comment = it.get("comments").toString()


                var txt_title: TextView = findViewById(R.id.textView14)
                var txt_description:TextView = findViewById(R.id.textView15)
                var txt_points:TextView = findViewById(R.id.textView13)
                var txt_comments:EditText = findViewById(R.id.messageTextField2)

                txt_title.text = title.toString()
                txt_description.text = description.toString()
                txt_points.text = points.toString()
                txt_comments.setText(comment.toString())

            }


        btn_submit.setOnClickListener(){

            if(status != "Submitted"){

                if(uploaded) {

                    userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).update("score", points.toString())
                    userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).update("status", "Submitted")
                    userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).update("comments", comments.text.toString())
                    userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).update("file", urlImagen)

                    Toast.makeText(baseContext, "Assigment Subimitted Successfully ", Toast.LENGTH_LONG).show()

                    finish()
                }else{
                    Toast.makeText(baseContext, "You haven't upload a file ", Toast.LENGTH_LONG).show()
                }
            }
            else{
                Toast.makeText(baseContext, "You already submitted the assignment", Toast.LENGTH_LONG).show()
            }

        }

    }

    //Photos & Files
    private fun FileManager() {
        //ABRE LA VENTA DEL FILENAMAGER PARA SELECCIONAR LA IMAGEN
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 ){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        }
        intent.type = "*/*"
        startActivityForResult(intent,fileResult)
    }

    //trae el elemento seleccionado del file manager
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {

                val clipData = data.clipData

                if (clipData != null){
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        uri?.let { fileUpload(it) }
                    }
                }else {
                    val uri = data.data
                    uri?.let { fileUpload(it) }
                }

            }
        }
    }

    private fun fileUpload(mUri: Uri) {
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Tasks")
        val path =mUri.lastPathSegment.toString()
        val fileName: StorageReference = folder.child(path.substring(path.lastIndexOf('/')+1))

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                urlImagen =java.lang.String.valueOf(uri)


                // Turn Boolean true
                uploaded = true

                Toast.makeText(baseContext, "File added succesfully", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }
}