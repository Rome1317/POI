package com.example.birdline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.models.Users
import com.squareup.picasso.Picasso

class ContactsAdapter(val context: Context, var LISTA:List<Users>): RecyclerView.Adapter<ContactsAdapter.Holder>() {


    inner class Holder(val view: View):RecyclerView.ViewHolder(view), View.OnClickListener{

        lateinit var  email:String
        lateinit var id:String
        lateinit var name:String

        var otherUser = ""
        var desti = ""
        var NAME = ""

        fun Draw (superHero: Users) {
            var email: TextView = view?.findViewById(R.id.username)
            var status: TextView = view?.findViewById(R.id.showMore)
            var img: ImageView = view?.findViewById(R.id.photo)


            if (superHero != null) {
                email.text = superHero.emails
                status.text = superHero.estado
                Picasso.get().load(superHero.image).into(img)

            }
        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(layoutInflater.inflate(R.layout.contact,parent,false))
    }

    //Cantidad de listas a dibujar
    override fun getItemCount(): Int {
        return LISTA.size
    }

    //Llena la info
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.Draw(LISTA[position])
    }
}

