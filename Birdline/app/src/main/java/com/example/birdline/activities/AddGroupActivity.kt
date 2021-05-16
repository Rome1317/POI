package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.birdline.R
import com.example.birdline.models.Grupos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class AddGroupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    //private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase  = FirebaseFirestore.getInstance()

    private  lateinit  var destinatario: EditText
    private  lateinit var txtgroupname: EditText

    lateinit var users: ArrayList<String>

    var members: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)
        auth = FirebaseAuth.getInstance()

        txtgroupname = findViewById(R.id.GroupName)

        var usersSpinner: Spinner = findViewById(R.id.spinner6)

        val postRef = firebase.collection(ReferenciasFirebase.USUARIOS.toString())
        postRef.get().addOnSuccessListener {result ->

            for (document in result) {
                members.add(document.data.getValue("id").toString())
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, members)
            usersSpinner.adapter = adapter
        }

        var btn_enviar: Button = findViewById(R.id.btn_addGroup)

        var btn_addmember: FloatingActionButton = findViewById(R.id.btn_AddMemberGroup)

        users = arrayListOf<String>(auth.currentUser.email)

        btn_enviar.setOnClickListener(){

            val test = txtgroupname.text.toString()

            if(test != "") {
                sendMessage()
                finish()
            }
            else{
                Toast.makeText(baseContext, "Enter a group name", Toast.LENGTH_LONG).show()
            }

        }

        btn_addmember.setOnClickListener(){

            val email = usersSpinner.getSelectedItem().toString()

            if(email in users) {
                Toast.makeText(this, "Member already added", Toast.LENGTH_SHORT).show()
            }else{
                users.add(email)
                usersSpinner.setSelection(0)
                Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()

                if(users.size >= 5) {
                    btn_enviar.isEnabled = true
                }
            }
        }

    }

    private fun sendMessage() {
        var groupId= UUID.randomUUID()
        val title = txtgroupname.text.toString()
        //val users = listOf(auth.currentUser.email, otherUser)
        val grupo = Grupos(
            id = groupId.toString(),
            name = "$title",
            users = users
        )

        firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(groupId.toString()).set(grupo)

        for(item in users){
            firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(item).collection(
                ReferenciasFirebase.GRUPOS.toString()).document(groupId.toString()).set(grupo)
        }

        /*
        firebase.collection(ReferenciasFirebase.USERS.toString()).document(auth.currentUser.email).collection(
            ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)
        firebase.collection(ReferenciasFirebase.USERS.toString()).document(otherUser).collection(
            ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)

         */

    }
}