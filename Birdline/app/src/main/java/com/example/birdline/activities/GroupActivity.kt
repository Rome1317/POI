package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.adapters.MessageAdapter
import com.example.birdline.adapters.PostAdapter
import com.example.birdline.models.Mensajes
import com.example.birdline.models.Post
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase

class GroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    private var opcion: String = "Post"
    private lateinit var _post: EditText
    private lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group)

        auth = FirebaseAuth.getInstance()

        var uid = intent.getStringExtra("ID")
        var chatname = intent.getStringExtra("GROUP_NAME")

        var groupname: TextView = findViewById(R.id.groupname2)
        groupname.text = chatname

        _post = findViewById(R.id.postTextField)
        if (uid != null) {
            _id = uid
        }

        val btn_send: Button = findViewById(R.id.sendPostButton)
        val btn_Post: Button = findViewById(R.id.btn_post)
        val btn_subGrupos: Button = findViewById(R.id.btn_subgroups)

        val contenedor: ConstraintLayout = findViewById(R.id.constraintLayout2)
        val btnF_addSubGroup: FloatingActionButton = findViewById(R.id.btnF_add_subgroup)

        btn_send.setOnClickListener() {
            sendPost()
        }

        btnF_addSubGroup.setOnClickListener() { //Boton de agregar sub grupo
            showActivity()
        }

        btn_Post.setOnClickListener() {

            opcion = "Post"
            showPosts()
            contenedor.visibility = View.VISIBLE
            btnF_addSubGroup.visibility = View.INVISIBLE

        }
        btn_subGrupos.setOnClickListener() {

            opcion = "SubGrupo"
            showSubGroups()
            contenedor.visibility = View.GONE
            btnF_addSubGroup.visibility = View.VISIBLE

        }

        val postRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id) //Estoy en duda si es POST xd
        val userRef = firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id)

        //val _listUsers:ArrayList<String>
        //_listUsers = (ArrayList<String>)postRef.get("users")

        if(opcion == "Post"){
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
        else if(opcion == "SubGrupo"){
            userRef.collection(ReferenciasFirebase.MENSAJES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                    .addSnapshotListener(){
                        messages,error ->
                        if (error == null){
                            messages?.let { var listChats = it.toObjects(Mensajes::class.java)
                                var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                                rv.layoutManager   = LinearLayoutManager(this)
                                val adapter = MessageAdapter(this, listChats)
                                rv.adapter = adapter }
                        }
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

        firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id).collection(ReferenciasFirebase.POST.toString()).document().set(post)
        txt_post_main.setText("")
    }


    private fun showPosts() {
        val postRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id) //Estoy en duda si es POST xd

        postRef.collection(ReferenciasFirebase.POST.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { document ->
                    var listPost = document.toObjects(Post::class.java)
                    var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                    rv.layoutManager   = LinearLayoutManager(this)
                    val adapter = PostAdapter(this, listPost) //Cambiar por PostAdapter, primero hay que crearlo
                    rv.adapter = adapter

                }
        //postRef.collection(ReferenciasFirebase.POST.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
        //        .addSnapshotListener(){
        //            messages,error ->
        //            if (error == null){
        //                messages?.let { var listPost = it.toObjects(Post::class.java)
        //                    var rv = findViewById<RecyclerView>(R.id.postRecyclerView)
//
        //                    rv.layoutManager   = LinearLayoutManager(this)
        //                    val adapter = PostAdapter(this, listPost)
        //                    rv.adapter = adapter }
        //            }
        //        }
    }
    private fun showSubGroups() {
        val userRef = firebase.collection(ReferenciasFirebase.CHATS.toString()).document(_id)

        userRef.collection(ReferenciasFirebase.MENSAJES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { document ->
                    var listChats = document.toObjects(Mensajes::class.java)
                    var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                    rv.layoutManager   = LinearLayoutManager(this)
                    val adapter = MessageAdapter(this, listChats)
                    rv.adapter = adapter



                }
        //userRef.collection(ReferenciasFirebase.MENSAJES.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
        //        .addSnapshotListener(){
        //            messages,error ->
        //            if (error == null){
        //                messages?.let { var listChats = it.toObjects(Mensajes::class.java)
        //                    var rv = findViewById<RecyclerView>(R.id.postRecyclerView)
//
        //                    rv.layoutManager   = LinearLayoutManager(this)
        //                    val adapter = MessageAdapter(this, listChats)
        //                    rv.adapter = adapter }
        //            }
        //        }
    }

    private fun showActivity(){
        val intent: Intent = Intent(this, AddSubGroupActivity::class.java)
        startActivity(intent)
        //finish()
    }
}