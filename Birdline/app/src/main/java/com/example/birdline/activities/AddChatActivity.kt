package com.example.birdline.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.birdline.models.Chat
import com.example.birdline.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList

class AddChatActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    //private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase  = FirebaseFirestore.getInstance()

    private  lateinit var chatname: EditText

    lateinit var users: ArrayList<String>

    var members: MutableList<String> = arrayListOf()

    //Photo
    private val fileResult = 1
    var urlImagen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_chat)

        // Choose File
        val getFile = findViewById<FloatingActionButton>(R.id.btn_Addphoto)

        getFile.setOnClickListener {
            FileManager()
        }

        auth = FirebaseAuth.getInstance()

        chatname = findViewById(R.id.ChatName)

        var usersSpinner: Spinner = findViewById(R.id.spinner4)

        val postRef = firebase.collection(ReferenciasFirebase.USUARIOS.toString())
        postRef.get().addOnSuccessListener {result ->

            for (document in result) {
                members.add(document.data.getValue("id").toString())
            }

            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, members)
            usersSpinner.adapter = adapter
        }


        var btn_enviar: Button = findViewById(R.id.btn_addchat)

        var btn_addmember: FloatingActionButton = findViewById(R.id.btn_AddMember)

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

            val email = usersSpinner.getSelectedItem().toString()

            if(email in users) {
                Toast.makeText(this, "Member already added", Toast.LENGTH_SHORT).show()
            }else{
                users.add(email)
                usersSpinner.setSelection(0)
                Toast.makeText(this, "Member added successfully", Toast.LENGTH_SHORT).show()

                if(users.size >= 2) {
                    btn_enviar.isEnabled = true
                }
            }
        }

    }

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
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Chats")
        val path =mUri.lastPathSegment.toString()
        val fileName: StorageReference = folder.child(path.substring(path.lastIndexOf('/')+1))

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                urlImagen =java.lang.String.valueOf(uri)

                // Turn Boolean true
                Toast.makeText(baseContext, "Photo added succesfully", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }


    private fun sendMessage() {
        var chatId= UUID.randomUUID()
        val title = chatname.text.toString()
        //val users = listOf(auth.currentUser.email, otherUser)
        val chat = Chat(
            id = chatId.toString(),
            name = "$title",
            users = users,
                image = urlImagen
        )

        firebase.collection(ReferenciasFirebase.CHATS.toString()).document(chatId.toString()).set(chat)

        for(item in users){
            firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(item).collection(
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