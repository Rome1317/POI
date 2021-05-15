package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.adapters.PostAdapter
import com.example.birdline.models.Post
import com.example.birdline.models.ReferenciasFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SubGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    private lateinit var _post: EditText
    private lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subgroup_post)

        auth = FirebaseAuth.getInstance()

        var uid = intent.getStringExtra("ID")
        var SubGroupName = intent.getStringExtra("SubGroup")

        var subgroup: TextView = findViewById(R.id.subgroupname)
        subgroup.text = SubGroupName

        _post = findViewById(R.id.postTextField)
        if (uid != null) {
            _id = uid
        }

        val btn_send: Button = findViewById(R.id.sendPostButton)

        btn_send.setOnClickListener() {
            sendPost()
        }

        val postRef = firebase.collection(ReferenciasFirebase.SUBGRUPOS.toString()).document(uid.toString())

        postRef.collection(ReferenciasFirebase.POST.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { document ->
                var listPost = document.toObjects(Post::class.java)
                var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                rv.layoutManager   = LinearLayoutManager(this)
                val adapter = PostAdapter(this, listPost) //Cambiar por PostAdapter, primero hay que crearlo
                rv.adapter = adapter

            }

        postRef.collection(ReferenciasFirebase.POST.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
            .addSnapshotListener(){
                    messages,error ->
                if (error == null){
                    messages?.let { var listPost = it.toObjects(Post::class.java)
                        var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                        rv.layoutManager   = LinearLayoutManager(this)
                        val adapter = PostAdapter(this, listPost)
                        rv.adapter = adapter }
                }
            }
    }

    private fun sendPost() {
        val publicacion = _post.text.toString()
        val txt_post_main: EditText = findViewById(R.id.postTextField)

        val post = Post(
            publicacion = publicacion,
            from = auth.currentUser.email
        )

        firebase.collection(ReferenciasFirebase.SUBGRUPOS.toString()).document(_id).collection(
            ReferenciasFirebase.POST.toString()).document().set(post)
        txt_post_main.setText("")
    }
}