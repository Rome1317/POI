package com.example.birdline.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.adapters.*
import com.example.birdline.models.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    private var opcion: String = "Post"
    private lateinit var _post: EditText
    private lateinit var _id: String

    lateinit var sub_groups: List<SubGrupos>
    private var adapter: SubGroupAdapter? = null

    //Photo & Files
    private val fileResult = 1
    var urlImagen = ""

    //Encryption
    private var ENCRYPT = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group)

        // Choose File
        val getFile = findViewById<Button>(R.id.filebtn4)

        getFile.setOnClickListener {
            FileManager()
        }


        // Set Encryption
        val toggle:Switch = findViewById(R.id.switch2)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            ENCRYPT = isChecked
        }

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
        val btn_tasks: Button = findViewById(R.id.button2)

        var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

        val SpinnerMembers: Spinner = findViewById(R.id.spinner)
        val members: TextView = findViewById(R.id.textView4)

        val btn_members: Button = findViewById(R.id.members)

        val contenedor: ConstraintLayout = findViewById(R.id.constraintLayout2)
        val btnF_addSubGroup: FloatingActionButton = findViewById(R.id.btnF_add_subgroup)
        val btnF_addTask: FloatingActionButton = findViewById(R.id.btnF_add_task)

        btn_send.setOnClickListener() {

            if(_post.text.toString() != ""){

                if (!ENCRYPT) {
                    sendPost()
                } else {
                    sendEncryptedPost()
                }
            }
            else{
                Toast.makeText(this,"Write something", Toast.LENGTH_SHORT).show()
            }

        }

        btnF_addSubGroup.setOnClickListener() { //Boton de agregar sub grupo
            showActivity()
        }

        btnF_addTask.setOnClickListener() { //Boton de agregar tarea
            showAddTask()
        }


        btn_Post.setOnClickListener() {

            opcion = "Post"
            showPosts()
            contenedor.visibility = View.VISIBLE
            btnF_addSubGroup.visibility = View.GONE
            btnF_addTask.visibility = View.GONE
            SpinnerMembers.visibility = View.GONE
            members.visibility = View.GONE
            rv.visibility = View.VISIBLE

        }
        btn_subGrupos.setOnClickListener() {

            opcion = "SubGrupo"
            showSubGroups()
            contenedor.visibility = View.GONE
            btnF_addSubGroup.visibility = View.VISIBLE
            btnF_addTask.visibility = View.GONE
            SpinnerMembers.visibility = View.GONE
            members.visibility = View.GONE
            rv.visibility = View.VISIBLE

        }
        btn_tasks.setOnClickListener {
            opcion = "Tasks"
            showTasks()
            contenedor.visibility = View.GONE
            btnF_addSubGroup.visibility = View.GONE
            btnF_addTask.visibility = View.VISIBLE
            SpinnerMembers.visibility = View.GONE
            members.visibility = View.GONE
            rv.visibility = View.VISIBLE
        }



        btn_members.setOnClickListener() {


            val postRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id)
            postRef.get().addOnSuccessListener {
                var users = it.get("users") as List<String>

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
                SpinnerMembers.adapter = adapter
            }

            opcion = "Members"
            rv.visibility = View.GONE
            SpinnerMembers.visibility = View.VISIBLE
            members.visibility = View.VISIBLE
            contenedor.visibility = View.GONE
            btnF_addSubGroup.visibility = View.GONE
            btnF_addTask.visibility = View.GONE
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

        if(opcion == "SubGrupo"){

            postRef.collection(ReferenciasFirebase.SUBGRUPOS.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                    .addSnapshotListener(){
                        messages,error ->
                        if (error == null){
                            messages?.let { var listChats = it.toObjects(SubGrupos::class.java)
                                var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                                rv.layoutManager   = LinearLayoutManager(this)
                                val adapter = SubGroupAdapter(this, listChats)
                                rv.adapter = adapter }
                        }
                    }
        }

        if(opcion == "Tasks"){

            postRef.collection(ReferenciasFirebase.TASKS.toString()).orderBy("dob",com.google.firebase.firestore.Query.Direction.ASCENDING)
                .addSnapshotListener(){
                        messages,error ->
                    if (error == null){
                        messages?.let { var listChats = it.toObjects(Assigments::class.java)
                            var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                            rv.layoutManager   = LinearLayoutManager(this)
                            val adapter = AssignmentAdapter(this, listChats)
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

    private fun sendEncryptedPost() {
        val publicacion = _post.text.toString()
        val txt_post_main: EditText = findViewById(R.id.postTextField)

        var encryted = Encriptacion.cifrar(publicacion,EncryptionKeys.POSTS.toString())

        val post = Post(
                publicacion = encryted,
                from = auth.currentUser.email,
                encrypted = true
        )

        firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id).collection(ReferenciasFirebase.POST.toString()).document().set(post)
        txt_post_main.setText("")
    }

    //Photos & Files
    private fun FileManager() {
        //ABRE LA VENTA DEL FILENAMAGER PARA SELECCIONAR LA IMAGEN
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 ){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        }
        intent.type = "*/*"
        startActivityForResult(intent,fileResult)
    }

    //trae el elemento seleccionado del file manager
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fileResult) {
            if (resultCode == RESULT_OK && data != null) {

                val clipData = data.clipData

                if (clipData != null){
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        uri?.let { fileUpload(it) }
                    }
                }else {
                    val uri = data.data
                    uri?.let { fileUpload(it) }
                }

            }
        }
    }

    private fun fileUpload(mUri: Uri) {
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Posts")
        val path =mUri.lastPathSegment.toString()
        val fileName: StorageReference = folder.child(path.substring(path.lastIndexOf('/')+1))

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                urlImagen =java.lang.String.valueOf(uri)

                _post.setText(urlImagen)

                // Turn Boolean true
                Toast.makeText(baseContext, "File added succesfully", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }
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

    }

    private fun showSubGroups() {
        val postRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id)

        postRef.collection(ReferenciasFirebase.SUBGRUPOS.toString())
                .get()
                .addOnSuccessListener { document ->
                    var listChats = document.toObjects(SubGrupos::class.java)
                    var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                    rv.layoutManager   = LinearLayoutManager(this)
                    val adapter = SubGroupAdapter(this, listChats)
                    rv.adapter = adapter



                }

    }

    private fun showTasks() {

        val postRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(_id)

        postRef.collection(ReferenciasFirebase.TASKS.toString())
            .get()
            .addOnSuccessListener { document ->
                var listChats = document.toObjects(Assigments::class.java)
                var rv = findViewById<RecyclerView>(R.id.postRecyclerView)

                rv.layoutManager   = LinearLayoutManager(this)
                val adapter = AssignmentAdapter(this, listChats)
                rv.adapter = adapter

            }
    }



    private fun showActivity(){
        val intent: Intent = Intent(this, AddSubGroupActivity::class.java)
        intent.putExtra("group_id", _id)
        startActivity(intent)
        //finish()
    }

    private fun showAddTask(){
        val intent: Intent = Intent(this, AddAssignmentActivity::class.java)
        intent.putExtra("group_id", _id)
        startActivity(intent)
        //finish()
    }

    override fun onResume() {
        super.onResume()

        if(opcion == "SubGrupo"){
            showSubGroups()
        }

        if(opcion == "Tasks"){
            showTasks()
        }
    }

}