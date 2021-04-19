package com.example.birdline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.birdline.frags.*
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    fun changeFrag(newFrag: Fragment, tag: String){

        val fragmentBefore = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentBefore == null ) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, newFrag)
                    .commit()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    changeFrag(FragmentoA(), tag = "FragmentA")
                }
                R.id.optCont -> { //Abre el fragmento B
                    changeFrag(FragmentoB(), tag = "FragmentB")
                }
                R.id.optChat -> { //Abre el fragmento C
                    changeFrag(FragmentoC(), tag = "FragmentC")
                }
                R.id.optAssigments -> { //Abre el fragmento G
                    changeFrag(FragmentoG(), tag = "FragmentG")
                }
                R.id.optGeneral -> { //Abre el fragmento D
                    changeFrag(FragmentoD(), tag = "FragmentD")
                }
                R.id.optAbout -> { //Abre el fragmento E
                    changeFrag(FragmentoE(), tag = "FragmentE")
                }
                R.id.optLog -> { //Abre el fragmento F
                    changeFrag(FragmentoF(), tag = "FragmentF")
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