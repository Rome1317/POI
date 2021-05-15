package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.birdline.R
import com.example.birdline.models.ReferenciasFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddSubGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sub_group)

        auth = FirebaseAuth.getInstance()

        val txtSubGroupName: TextView = findViewById(R.id.SubGroupName)
        val SpinnerMember: Spinner = findViewById(R.id.spinnerMembers)

        var id = intent.getStringExtra("group_id")

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(id)


        userRef.get().addOnSuccessListener {
            var users = it.get("users") as List<String>

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
            SpinnerMember.adapter = adapter
        }


    }
}