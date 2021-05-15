package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.birdline.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.example.birdline.models.ReferenciasFirebase
import com.example.birdline.models.Users

class SignUpActivity : AppCompatActivity() {

    lateinit var btnSignUp: Button
    lateinit var etUser : EditText
    lateinit var etEmail : EditText
    lateinit var etPass : EditText
    lateinit var btnPhoto : Button

    private val auth = Firebase.auth

    private val db = FirebaseDatabase.getInstance() //INTANCIA DE LA BASE DE DATOS
    val firebase  = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Setup
        setup()


    }

    private fun setup(){
        title = "Autenticación"

        // Sign Up
        btnSignUp = findViewById<Button>(R.id.btnRegister)
        etUser = findViewById<EditText>(R.id.etUser)
        etEmail = findViewById<EditText>(R.id.etEmail)
        etPass = findViewById<EditText>(R.id.etPassword)
        btnPhoto = findViewById<Button>(R.id.btn_add_Photo)

        btnSignUp.setOnClickListener(){
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty() && etUser.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(etEmail.text.toString(),
                        etPass.text.toString()).addOnCompleteListener(){

                    if(it.isSuccessful){
                        val usuario = Users(
                                id = etEmail.text.toString(),
                                nombre = etUser.text.toString(),
                                emails = etEmail.text.toString(),
                                contraseña = etPass.text.toString(),
                                estado = "Disponible",
                                image = ""
                        )
                        firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(etEmail.text.toString()).set(usuario)
                        showHome(it.result?.user?.email ?: "")
                    }else{
                        it.exception.let{
                            Toast.makeText(baseContext, it?.message, Toast.LENGTH_LONG).show()
                        }
                        //showAlert()
                    }
                }
            }
            else{
                Toast.makeText(baseContext, "Ingrese todo los campos", Toast.LENGTH_LONG).show()
                //showAlert()
            }

        }



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
}