package com.example.birdline.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.adapters.GroupAdapter
import com.example.birdline.models.Grupos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase

class GroupListActicity : AppCompatActivity() {
    lateinit var btnContacto: Button
    lateinit var BtnAddGrupo: Button

    private lateinit var auth: FirebaseAuth
    val firebase  = FirebaseFirestore.getInstance();

    lateinit var MyGroups: List<Grupos>

    private var context2: Context? = null
    private var adapter: GroupAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_group_list)
        btnContacto = findViewById<Button>(R.id.button7)
        btnContacto.setOnClickListener(){
            showContactos()
        }

        BtnAddGrupo = findViewById<Button>(R.id.button8)
        BtnAddGrupo.setOnClickListener(){
            showAddGroup()
            finish()
        }

        // Inflate the layout for this fragment
        //var root = inflater.inflate(R.layout.fragment_chat_list, container, false)
        auth = FirebaseAuth.getInstance()

        val rvGroupList: RecyclerView = findViewById<RecyclerView>(R.id.list_groups)
        rvGroupList.layoutManager = LinearLayoutManager(this)

        val groupRef = firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(auth.currentUser.email)

        groupRef.collection(ReferenciasFirebase.GRUPOS.toString()).get()
                .addOnSuccessListener { document ->
                    MyGroups= document.toObjects(Grupos::class.java)
                    this.adapter = GroupAdapter(this, MyGroups)
                    rvGroupList.adapter = this.adapter
                }

        groupRef.collection(ReferenciasFirebase.GRUPOS.toString()).addSnapshotListener(){
            messages,error ->
            if (error == null){
                messages?.let {MyGroups= it.toObjects(Grupos::class.java)
                    this.adapter = GroupAdapter(this, MyGroups)
                    rvGroupList.adapter = this.adapter }
            }
        }
    }

    fun showContactos(){
        //val intent: Intent = Intent(this, frag_b::class.java)
        //startActivity(intent)
        finish()
    }

    fun showAddGroup(){
        val intent: Intent = Intent(this, AddGroupActivity::class.java)
        startActivity(intent)
    }


}