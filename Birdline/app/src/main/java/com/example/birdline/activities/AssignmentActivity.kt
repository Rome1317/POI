package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.birdline.R
import com.example.birdline.models.ReferenciasFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AssignmentActivity : AppCompatActivity() {

    val firebase  = FirebaseFirestore.getInstance();

    lateinit var points:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment)

        var task_id = intent.getStringExtra("taskid")
        var group_id = intent.getStringExtra("groupid")

        var btn_submit: Button = findViewById(R.id.btn_uploadtask)

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(group_id.toString())

        userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).get()
            .addOnSuccessListener{
                var id = it.get("id").toString()
                var title = it.get("title").toString()
                var description = it.get("description").toString()
                points = it.get("points").toString()
                var score = it.get("score").toString()
                var status = it.get("status").toString()
                var users = it.get("users").toString()


                var txt_title: TextView = findViewById(R.id.textView14)
                var txt_description:TextView = findViewById(R.id.textView15)
                var txt_points:TextView = findViewById(R.id.textView13)

                txt_title.text = title.toString()
                txt_description.text = description.toString()
                txt_points.text = points.toString()


            }


        btn_submit.setOnClickListener(){

            userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).update("score",points.toString())
            userRef.collection(ReferenciasFirebase.TASKS.toString()).document(task_id.toString()).update("status","Submitted")

            finish()
        }

    }
}