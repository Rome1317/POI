package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.alonsodelcid.multichat.models.Chat
import com.example.birdline.R
import com.example.birdline.models.Mensajes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.poi.camppus.models.ReferenciasFirebase
import java.util.*
import kotlin.collections.ArrayList

class AddChatActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase  = FirebaseFirestore.getInstance()

    private  lateinit  var destinatario: EditText
    private  lateinit var chatname: EditText

    lateinit var users: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chat)
        auth = FirebaseAuth.getInstance()

        chatname = findViewById(R.id.ChatName)
        destinatario = findViewById(R.id.txt_SendTo)


        var btn_enviar: Button = findViewById(R.id.btn_addchat)

        var btn_addmember: Button = findViewById(R.id.btn_AddMember)

        users = arrayListOf<String>(auth.currentUser.email)

        btn_enviar.setOnClickListener(){

            val test = chatname.text.toString()

            if(test != "") {
                sendMessage()
                finish()
            }
            else{
                Toast.makeText(baseContext, "Enter a chat name", Toast.LENGTH_LONG).show()
            }

        }

        btn_addmember.setOnClickListener(){

            val email = destinatario.text.toString()

            if(email != "") {
                users.add(email)
                destinatario.setText("")

                Toast.makeText(baseContext, "Member added successfully", Toast.LENGTH_LONG).show()

                btn_enviar.isEnabled = true
            }
            else{
                Toast.makeText(baseContext, "Add a member to chat with", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun sendMessage() {
        var chatId= UUID.randomUUID()
        val title = chatname.text.toString()
        //val users = listOf(auth.currentUser.email, otherUser)
        val chat = Chat(
            id = chatId.toString(),
            name = "$title",
            users = users
        )

        firebase.collection(ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)

        for(item in users){
            firebase.collection(ReferenciasFirebase.USERS.toString()).document(item).collection(
                ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)
        }

        /*
        firebase.collection(ReferenciasFirebase.USERS.toString()).document(auth.currentUser.email).collection(
            ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)
        firebase.collection(ReferenciasFirebase.USERS.toString()).document(otherUser).collection(
            ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)

         */

    }
}