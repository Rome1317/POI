package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.birdline.R

class FirstActivity : AppCompatActivity() {

    lateinit var btnSignUp: Button
    lateinit var btnSignIn: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        Thread.sleep(2000)
        setTheme(R.style.Theme_Birdline)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe1)

        // Sign Up
        btnSignUp = findViewById<Button>(R.id.btnSignup)

        btnSignUp.setOnClickListener(){
            showSignup()
        }

        // Sign In
        btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnSignIn.setOnClickListener(){
            showLogin()
        }
    }

    fun showSignup(){
        val intent: Intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showLogin(){
        val intent: Intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)
    }
}