package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.birdline.R
import com.example.birdline.models.Assigments
import com.example.birdline.models.ReferenciasFirebase
import com.google.android.gms.tasks.Tasks
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class AddAssignmentActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val firebase  = FirebaseFirestore.getInstance()

    lateinit var  tasktitle:EditText
    lateinit var  taskdescription:EditText
    lateinit var  taskpoints: EditText

    lateinit var userstoadd: ArrayList<String>

    lateinit var group_id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_assignment)

        var id = intent.getStringExtra("group_id").toString()

        group_id = id

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(group_id)

        userRef.get().addOnSuccessListener {
            userstoadd = it.get("users") as ArrayList<String>
        }

        tasktitle = findViewById(R.id.editTextTitle)
        taskdescription = findViewById(R.id.editTextTextMultiLine)
        taskpoints = findViewById(R.id.editPoints)
        var btnadd: FloatingActionButton = findViewById(R.id.btn_AddTask)


        btnadd.setOnClickListener() {

            val title = tasktitle.text.toString()
            val desc = taskdescription.text.toString()
            val points = taskpoints.text.toString()

            if(title != "" && desc != "" && points != ""){

                val assigmnent = Assigments(
                    id = UUID.randomUUID().toString(),
                    title =  tasktitle.text.toString(),
                    description = taskdescription.text.toString(),
                    points = taskpoints.text.toString(),
                    users = userstoadd,
                    grupo_id = group_id
                )

                createTask(assigmnent)
                finish()

            }else{
                Toast.makeText(baseContext, "Complete all fields", Toast.LENGTH_LONG).show()
            }

        }

    }

    private fun createTask(assigmnent: Assigments){

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(group_id)

        userRef.collection(ReferenciasFirebase.TASKS.toString()).document(assigmnent.id).set(assigmnent)

        /*
        for(item in assigmnent.users){
            firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(item).collection(
                ReferenciasFirebase.TASKS.toString()).document(assigmnent.id).set(assigmnent)
        }

         */
    }


}