package com.example.birdline.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.birdline.R
import com.example.birdline.frags.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase
import com.squareup.picasso.Picasso

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

        var id = intent.getStringExtra("email")

        val myNav = findViewById<NavigationView>(R.id.nav)
        val myDrawer = findViewById<DrawerLayout>(R.id.drawer)

        val headerView: View = myNav.getHeaderView(0)

        var email:TextView = headerView.findViewById<TextView>(R.id.txtCorreo)
        email.text = id.toString()

        val postRef = firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(id)

        postRef.get().addOnSuccessListener {
            var username = it.get("nombre").toString()
            var image = it.get("image").toString()

            var name:TextView = headerView.findViewById<TextView>(R.id.tvUser)
            name.text = username

            var photo:ImageView = headerView.findViewById<ImageView>(R.id.imageView4)
            Picasso.get().load(image).into(photo)
        }





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
                    firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(id).update("estado","Desconectado")
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