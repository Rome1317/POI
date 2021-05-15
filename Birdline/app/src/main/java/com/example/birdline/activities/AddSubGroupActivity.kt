package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.birdline.R
import com.example.birdline.models.ReferenciasFirebase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddSubGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    lateinit var userstoadd: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sub_group)

        auth = FirebaseAuth.getInstance()

        val txtSubGroupName: TextView = findViewById(R.id.SubGroupName)
        val SpinnerMember: Spinner = findViewById(R.id.spinnerMembers)
        val addMember: FloatingActionButton = findViewById(R.id.addSubMember)

        userstoadd = arrayListOf<String>(auth.currentUser.email)

        var id = intent.getStringExtra("group_id")

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(id)

        userRef.get().addOnSuccessListener {
            var users = it.get("users") as List<String>

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
            SpinnerMember.adapter = adapter
        }

        addMember.setOnClickListener(){
            val email = SpinnerMember.getSelectedItem().toString()

            if(email != null){
                if(email in userstoadd){
                    Toast.makeText(this, "Member already added", Toast.LENGTH_SHORT).show()

                }else{
                    userstoadd.add(email)
                    SpinnerMember.setSelection(0)
                    Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(baseContext, "Select a Member to add", Toast.LENGTH_LONG).show()
            }



        }


    }
}