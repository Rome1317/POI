package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alonsodelcid.multichat.models.Chat
import com.example.birdline.R
import com.example.birdline.adapters.ChatAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.*

class ChatListActivity : AppCompatActivity() {

    var clase = listOf<Chat>(
        Chat("1","alberto", listOf("sd")),
        Chat("1","daniel", listOf("sd"))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_list)

        var rv_superhero = findViewById<RecyclerView>(R.id.listChatsRecyclerView)

        rv_superhero.layoutManager   = LinearLayoutManager(this)
        val adapter = ChatAdapter(this, clase)
        rv_superhero.adapter = adapter
    }

}