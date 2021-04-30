package com.example.birdline.models

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alonsodelcid.multichat.models.Chat
import com.example.birdline.R
import com.example.birdline.adapters.ChatAdapter

class Test : AppCompatActivity() {

    var chats :List<Chat> = listOf(
        Chat("1","alberto", listOf("sd")),
        Chat("1","daniel", listOf("sd"))
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_chat_list)

        var rv_superhero = findViewById<RecyclerView>(R.id.listChatsRecyclerView)

        rv_superhero.layoutManager   = LinearLayoutManager(this)
        val adapter = ChatAdapter(this, chats)
        rv_superhero.adapter = adapter
    }
}