package com.example.birdline.adapters

import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.activities.SubGroupActivity
import com.example.birdline.models.ReferenciasFirebase
import com.example.birdline.models.SubGrupos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SubGroupAdapter(val context: Context, var LISTA: List<SubGrupos>): RecyclerView.Adapter<SubGroupAdapter.Holder>() {
    val firebase  = FirebaseFirestore.getInstance();


    inner class Holder(val view: View):RecyclerView.ViewHolder(view),  View.OnClickListener{
        private lateinit var auth: FirebaseAuth

        lateinit var Subgroupname:String
        lateinit var id:String
        lateinit var group_id:String

        fun render(subgroup: SubGrupos) {
            auth = FirebaseAuth.getInstance()

            var team: TextView = view?.findViewById(R.id.groupname)

            //var imagen:ImageView =  view?.findViewById(R.id.img_subgrupo)
            //Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/camppus-224af.appspot.com/o/IconTeams.jpg?alt=media&token=d1c3f480-9de1-438a-bd42-3046de17480a").into(imagen)

            team.text = subgroup.name
            this.Subgroupname = subgroup.name
            this.id = subgroup.id
            this.group_id = subgroup.grupo_id

        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.OpenGroupChat -> {

                    val userRef = firebase.collection(ReferenciasFirebase.GRUPOS.toString()).document(this.group_id.toString())

                    userRef.collection(ReferenciasFirebase.SUBGRUPOS.toString()).document(this.id.toString()).get()
                            .addOnSuccessListener {
                                var members = it.get("users")  as ArrayList<String>

                                if(auth.currentUser.email in members){
                                    val  activityIntent =  Intent(context, SubGroupActivity::class.java)
                                    //Mandar datos
                                    activityIntent.putExtra("group_id",this.group_id)
                                    activityIntent.putExtra("id",this.id)
                                    activityIntent.putExtra("SubGroup", this.Subgroupname)
                                    context.startActivity(activityIntent)
                                }else{
                                    Toast.makeText(context.applicationContext,"You don't have access to this subgroup", Toast.LENGTH_SHORT).show()
                                }
                            }

                    }
                }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(layoutInflater.inflate(R.layout.contact_group, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(LISTA[position])
    }

    override fun getItemCount(): Int {
        return LISTA.size
    }
}