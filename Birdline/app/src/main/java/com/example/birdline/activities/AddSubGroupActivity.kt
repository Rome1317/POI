package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.birdline.R
import com.example.birdline.models.Grupos
import com.example.birdline.models.ReferenciasFirebase
import com.example.birdline.models.SubGrupos
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

class AddSubGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    lateinit var userstoadd: ArrayList<String>
    lateinit var txtSubGroupName: EditText

    lateinit var group_id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_sub_group)

        auth = FirebaseAuth.getInstance()

        txtSubGroupName = findViewById(R.id.SubGroupName)
        val SpinnerMember: Spinner = findViewById(R.id.spinnerMembers)
        val addMember: FloatingActionButton = findViewById(R.id.addSubMember)
        val addSubgroup: Button = findViewById(R.id.btn_addSubGroup)

        userstoadd = arrayListOf<String>(auth.currentUser.email)

        var id = intent.getStringExtra("group_id")

        group_id = id

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

        addMember.setOnClickListener(){
            val email = SpinnerMember.getSelectedItem().toString()

            if(email != null){
                if(email in userstoadd){
                    Toast.makeText(this, "Member already added", Toast.LENGTH_SHORT).show()

                }else{
                    userstoadd.add(email)
                    SpinnerMember.setSelection(0)
                    Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()

                    if(userstoadd.size >= 2) {
                        addSubgroup.isEnabled = true
                    }

                }
            }
            else {
                Toast.makeText(baseContext, "Select a Member to add", Toast.LENGTH_LONG).show()
            }


        }

        addSubgroup.setOnClickListener(){

            val Subgroup_name = txtSubGroupName.text.toString()

            if(Subgroup_name != "") {
                createSubgroup()
                finish()
            }
            else{
                Toast.makeText(baseContext, "Enter Subgroup name", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun createSubgroup() {
        var groupId= UUID.randomUUID()
        val title = txtSubGroupName.text.toString()
        //val users = listOf(auth.currentUser.email, otherUser)
        val grupo = SubGrupos(
                id = groupId.toString(),
                name = "$title",
                users = userstoadd
        )

        firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(group_id).collection(ReferenciasFirebase.SUBGRUPOS.toString()).document(grupo!!.id).set(grupo!!)

        for(item in userstoadd){
            firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(item).collection(
                    ReferenciasFirebase.SUBGRUPOS.toString()).document(groupId.toString()).set(grupo)
        }

        /*
        firebase.collection(ReferenciasFirebase.USERS.toString()).document(auth.currentUser.email).collection(
            ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)
        firebase.collection(ReferenciasFirebase.USERS.toString()).document(otherUser).collection(
            ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)

         */

    }
}