package com.example.birdline.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.activities.AssignmentActivity
import com.example.birdline.models.Assigments
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AssignmentAdapter(val context: Context, var LISTA: List<Assigments>): RecyclerView.Adapter<AssignmentAdapter.Holder>() {
    val firebase  = FirebaseFirestore.getInstance();


    inner class Holder(val view: View):RecyclerView.ViewHolder(view),  View.OnClickListener{
        private lateinit var auth: FirebaseAuth

        lateinit var id:String
        lateinit var groupid:String

        fun render(task: Assigments) {
            auth = FirebaseAuth.getInstance()

            var title: TextView = view?.findViewById(R.id.title)
            var status: TextView = view?.findViewById(R.id.status)
            var score: TextView = view?.findViewById(R.id.textView10)
            var points: TextView = view?.findViewById(R.id.textView17)

                title.text = task.title
                status.text = task.status
                score.text = task.score
                points.text = task.points
                this.id = task.id
                this.groupid = task.grupo_id


        }
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.OpenAssigment -> {
                    val  activityIntent =  Intent(context, AssignmentActivity::class.java)
                    //Mandar datos
                    activityIntent.putExtra("taskid",this.id)
                    activityIntent.putExtra("groupid",this.groupid)
                    context.startActivity(activityIntent)

                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return Holder(layoutInflater.inflate(R.layout.assignments, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(LISTA[position])
    }

    override fun getItemCount(): Int {
        return LISTA.size
    }
}