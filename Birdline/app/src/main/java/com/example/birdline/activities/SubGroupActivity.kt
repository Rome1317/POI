package com.example.birdline.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.adapters.PostAdapter
import com.example.birdline.models.Encriptacion
import com.example.birdline.models.EncryptionKeys
import com.example.birdline.models.Post
import com.example.birdline.models.ReferenciasFirebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SubGroupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val firebase = FirebaseFirestore.getInstance();

    private lateinit var _post: EditText
    private lateinit var _id: String

    //Photo & Files
    private val fileResult = 1
    var urlImagen = ""

    //Encryption
    private var ENCRYPT = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subgroup_post)

        // Choose File
        val getFile = findViewById<Button>(R.id.filebtn3)

        getFile.setOnClickListener {
            FileManager()
        }

        // Set Encryption
        val toggle: Switch = findViewById(R.id.switch5)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            ENCRYPT = isChecked
        }

        auth = FirebaseAuth.getInstance()

        var group_id = intent.getStringExtra("group_id")
        var uid = intent.getStringExtra("id")
        var SubGroupName = intent.getStringExtra("SubGroup")

        val SpinnerMembers: Spinner = findViewById(R.id.spinner3)

        val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(group_id.toString())
        userRef.collection(ReferenciasFirebase.SUBGRUPOS.toString()).document(uid.toString()).get().
            addOnSuccessListener {
                var users = it.get("users") as List<String>

                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, users)
                SpinnerMembers.adapter = adapter
        }



        var subgroup: TextView = findViewById(R.id.subgroupname)
        subgroup.text = SubGroupName

        _post = findViewById(R.id.postTextField)
        if (uid != null) {
            _id = uid
        }

        val btn_send: Button = findViewById(R.id.sendPostButton)

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

    private fun sendEncryptedPost() {
        val publicacion = _post.text.toString()
        val txt_post_main: EditText = findViewById(R.id.postTextField)

        var encryted = Encriptacion.cifrar(publicacion, EncryptionKeys.POSTS.toString())

        val post = Post(
                publicacion = encryted,
                from = auth.currentUser.email,
                encrypted = true
        )

        firebase.collection(ReferenciasFirebase.SUBGRUPOS.toString()).document(_id).collection(ReferenciasFirebase.POST.toString()).document().set(post)
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
}