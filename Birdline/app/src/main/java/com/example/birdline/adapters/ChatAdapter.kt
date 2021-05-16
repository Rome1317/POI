package com.example.birdline.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.models.Chat
import com.example.birdline.R
import com.example.birdline.activities.ChatActivity
import com.squareup.picasso.Picasso

class ChatAdapter(val context: Context, var LISTA:List<Chat>): RecyclerView.Adapter<ChatAdapter.Holder>() {


    inner class Holder(val view: View):RecyclerView.ViewHolder(view), View.OnClickListener{

        lateinit var  email:String
        lateinit var id:String
        lateinit var name:String
        lateinit var image:String

        fun Draw (superHero: Chat) {
            var txt: TextView = view?.findViewById(R.id.username)
            var men: TextView = view?.findViewById(R.id.showMore)
            var img: ImageView = view?.findViewById(R.id.photo)

            men.text = superHero.users[0]+", "+superHero.users[1]
            email =superHero.users[1]
            id = superHero.id
            name = superHero.name
            txt.text= superHero.name

            if(superHero.image != "") {
                Picasso.get().load(superHero.image).into(img)
            }else{
                superHero.image = "https://firebasestorage.googleapis.com/v0/b/birdline-test.appspot.com/o/user.png?alt=media&token=3a58d58a-739e-45ee-9526-921dc24b1f29"
                Picasso.get().load(superHero.image).into(img)
            }

            image = superHero.image.toString()


        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.OpenChat -> {
                    val  activityIntent =  Intent(context, ChatActivity::class.java)
                    //Mandar datos
                    activityIntent.putExtra("EMAIL",this.email)
                    activityIntent.putExtra("ID",this.id)
                    activityIntent.putExtra("CHAT_NAME", this.name)
                    activityIntent.putExtra("PHOTO", this.image)
                    context.startActivity(activityIntent)
                }
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

