package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import android.widget.TextView
import com.example.birdline.R

class AddSubGroupActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sub_group)

        val txtSubGroupName: TextView = findViewById(R.id.SubGroupName)
        val SpinnerMember: Spinner = findViewById(R.id.spinnerMembers)
        //val txtSubGroupName: TextView = findViewById(R.id.SubGroupName)

        //val nameSubGroup
        //val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id)

        /*
        userRef.get().addOnSuccessListener{
            var users = it.get("users")
            grupo = Grupos.Subgrupos(
                    id = GrupoId.toString(),
                    name = nombreEquipo,
                    users = users as List<String>
            )

         */
    }
}