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
import com.google.firebase.ktx.Firebase
import com.example.birdline.models.ReferenciasFirebase

class LogInActivity : AppCompatActivity() {

    lateinit var btnLogin: Button
    lateinit var etEmail : EditText
    lateinit var etPass : EditText

    private val auth = Firebase.auth
    val firebase  = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Setup
        setup()
    }

    private fun setup(){
        title = "Autenticación"

        // Sign Up
        btnLogin = findViewById<Button>(R.id.btnRegister)
        etEmail = findViewById<EditText>(R.id.etEmail)
        etPass = findViewById<EditText>(R.id.etPassword)

        btnLogin.setOnClickListener(){
            if (etEmail.text.isNotEmpty() && etPass.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(etEmail.text.toString(),
                        etPass.text.toString()).addOnCompleteListener(){

                    if(it.isSuccessful){
                        firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(etEmail.text.toString()).update("estado","Disponible")
                        showHome(it.result?.user?.email ?: "")
                    }else{
                        it.exception.let{
                            Toast.makeText(baseContext, it?.message,Toast.LENGTH_LONG).show()
                        }
                        //showAlert()
                    }
                }
            }

        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
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