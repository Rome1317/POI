package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.birdline.R
import com.example.birdline.frags.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase

enum class ProviderType{
    BASIC
}

class MainActivity : AppCompatActivity(){

    val firebase  = FirebaseFirestore.getInstance()

    fun changeFrag(newFrag: Fragment, tag: String){

        val fragmentBefore = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentBefore == null ) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, newFrag)
                    .commit()
        }

    }

    //lateinit var spemails: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var user = intent.getStringExtra("user")

        val myNav = findViewById<NavigationView>(R.id.nav)
        val myDrawer = findViewById<DrawerLayout>(R.id.drawer)

        /*
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        //Icono hamburguesa
        val toogle = ActionBarDrawerToggle(this, myDrawer, toolbar, R.string.app_name, R.string.app_name)

        myDrawer.addDrawerListener(toogle)
        toogle.syncState()

         */

        myNav.setNavigationItemSelectedListener {

            when(it.itemId){

                R.id.optMeet -> { //Abre el fragmento A
                    //changeFrag(frag_a(), tag = "FragmentA")
                }
                R.id.optCont -> { //Abre el fragmento B
                    changeFrag(frag_contact_list(), tag = "FragmentB")
                }
                R.id.optChat -> { //Abre el fragmento C
                    changeFrag(frag_chat_list(), tag = "FragmentC")
                }
                R.id.optAssigments -> { //Abre el fragmento G
                    changeFrag(frag_g(), tag = "FragmentG")
                }
                R.id.optGeneral -> { //Abre el fragmento D
                    changeFrag(frag_chat(), tag = "FragmentD")
                }
                R.id.optAbout -> { //Abre el fragmento E
                    changeFrag(frag_settings(), tag = "FragmentE")
                }
                R.id.optLog -> { //Abre FirstActivity
                    firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(user).update("estado","Desconectado")
                    FirebaseAuth.getInstance().signOut()

                    val intent: Intent = Intent(this, FirstActivity::class.java)
                    finish()
                    startActivity(intent)

                }
                else -> {
                    TODO()
                }
            }

            myDrawer.closeDrawer(GravityCompat.START)

            true
        }

    }


}