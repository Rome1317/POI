package com.example.birdline.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import com.example.birdline.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.example.birdline.models.ReferenciasFirebase
import com.example.birdline.models.Users
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignUpActivity : AppCompatActivity() {

    lateinit var btnSignUp: Button
    lateinit var etUser : EditText
    lateinit var etEmail : EditText
    lateinit var etPass : EditText

    lateinit var btnback: ImageButton

    var photoAdded: Boolean = false


    private val auth = Firebase.auth

    private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase  = FirebaseFirestore.getInstance()

    private val fileResult = 1
    var urlImagen = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Choose File
        val getFile = findViewById<Button>(R.id.btn_add_Photo)

        getFile.setOnClickListener {
            FileManager()
        }


        // Setup
        setup()


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


    private fun setup(){
        title = "Autenticación"

        // Sign Up
        btnSignUp = findViewById<Button>(R.id.btnRegister)
        etUser = findViewById<EditText>(R.id.etUser)
        etEmail = findViewById<EditText>(R.id.etEmail)
        etPass = findViewById<EditText>(R.id.etPassword)

        btnback = findViewById<ImageButton>(R.id.btnback2)

        btnSignUp.setOnClickListener(){

            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty() && etUser.text.isNotEmpty()) {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        etEmail.text.toString(),
                        etPass.text.toString()
                ).addOnCompleteListener() {

                    if (it.isSuccessful) {
                        val usuario = Users(
                                id = etEmail.text.toString(),
                                nombre = etUser.text.toString(),
                                emails = etEmail.text.toString(),
                                contraseña = etPass.text.toString(),
                                estado = "Disponible",
                                image = urlImagen
                        )
                        firebase.collection(ReferenciasFirebase.USUARIOS.toString())
                                .document(etEmail.text.toString()).set(usuario)
                        showHome(it.result?.user?.email ?: "")
                    } else {
                        it.exception.let {
                            Toast.makeText(baseContext, it?.message, Toast.LENGTH_LONG).show()
                        }
                        //showAlert()
                    }
                }

            } else {
                Toast.makeText(baseContext, "Ingrese todo los campos", Toast.LENGTH_LONG).show()
                //showAlert()
            }

        }


        btnback.setOnClickListener(){

            showFirst()

        }

    }

    private fun showFirst(){
        val intent: Intent = Intent(this, FirstActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Ingrese todos los campos")
        //builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String){
        val currentUser = auth.currentUser
        val intent: Intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user", currentUser.email)
        startActivity(intent)
        finish()
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
        val folder: StorageReference = FirebaseStorage.getInstance().reference.child("Users")
        val path =mUri.lastPathSegment.toString()
        val fileName: StorageReference = folder.child(path.substring(path.lastIndexOf('/')+1))

        fileName.putFile(mUri).addOnSuccessListener {
            fileName.downloadUrl.addOnSuccessListener { uri ->
                urlImagen =java.lang.String.valueOf(uri)

                // Turn Boolean true
                photoAdded = true
                Toast.makeText(baseContext, "Photo added succesfully", Toast.LENGTH_LONG).show()

            }
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }
}