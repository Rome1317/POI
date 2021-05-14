package com.example.birdline.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.activities.ChatActivity
import com.example.birdline.activities.GroupActivity
import com.example.birdline.models.Grupos

class GroupAdapter(val context: Context, var LISTA:List<Grupos>): RecyclerView.Adapter<GroupAdapter.Holder>() {


    inner class Holder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{

        lateinit var  users: List<String>
        lateinit var id:String
        lateinit var name:String

        fun Draw (superHero: Grupos) {
            var txt: TextView = view?.findViewById(R.id.groupname)
            //var men: TextView = view?.findViewById(R.id.showMore)
            //men.text = superHero.users[0]+", "+superHero.users[1]
            //email =superHero.users[1]
            id = superHero.id
            name = superHero.name
            txt.text = superHero.name
        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.OpenGroupChat -> {
                    val  activityIntent =  Intent(context, GroupActivity::class.java)
                    //Mandar datos
                    //activityIntent.putExtra("EMAIL",this.email)
                    activityIntent.putExtra("ID",this.id)
                    activityIntent.putExtra("GROUP_NAME", this.name)
                    context.startActivity(activityIntent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(layoutInflater.inflate(R.layout.contact_group,parent,false))
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