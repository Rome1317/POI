package com.example.birdline.activities

import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alonsodelcid.multichat.models.Chat
import com.example.birdline.R
import com.example.birdline.adapters.MessageAdapter
import com.example.birdline.models.Mensajes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.poi.camppus.models.ReferenciasFirebase
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase = FirebaseFirestore.getInstance();

    private lateinit var _Mensaje:EditText
    private lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)

        auth = FirebaseAuth.getInstance()

        var ema = intent.getStringExtra("EMAIL")
        var uid = intent.getStringExtra("ID")
        var chatname = intent.getStringExtra("CHAT_NAME")

        var username: TextView = findViewById(R.id.textView9)
        username.text = chatname


        _Mensaje = findViewById(R.id.messageTextField)
        if (uid != null) {
            _id = uid
        }


        val btn_send: Button = findViewById(R.id.sendMessageButton)

        btn_send.setOnClickListener() {
            sendMessage()
        }

        val userRef = firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id)

        userRef.collection(ReferenciasFirebase.MESSAGES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { document ->
                    var listChats = document.toObjects(Mensajes::class.java)
                    var rv = findViewById<RecyclerView>(R.id.messagesRecyclerView)

                    rv.layoutManager   = LinearLayoutManager(this)
                    val adapter = MessageAdapter(this, listChats)
                    rv.adapter = adapter



                }
        userRef.collection(ReferenciasFirebase.MESSAGES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .addSnapshotListener(){
                    messages,error ->
                    if (error == null){
                        messages?.let { var listChats = it.toObjects(Mensajes::class.java)
                            var rv = findViewById<RecyclerView>(R.id.messagesRecyclerView)

                            rv.layoutManager   = LinearLayoutManager(this)
                            val adapter = MessageAdapter(this, listChats)
                            rv.adapter = adapter }
                    }
                }

    }

    private fun sendMessage() {
        val mensaje = _Mensaje.text.toString()
        val txt_mensaje_main:EditText = findViewById(R.id.messageTextField)

        val chat = Mensajes(
            message = mensaje,
            from = auth.currentUser.email

        )

        firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id).collection(ReferenciasFirebase.MESSAGES.toString()).document().set(chat)
        txt_mensaje_main.setText("")
    }
}