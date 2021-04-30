package com.example.birdline.adapters

import android.content.Context
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.models.Mensajes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MessageAdapter(val context: Context, var LISTA: List<Mensajes>): RecyclerView.Adapter<MessageAdapter.Holder>(){
    val firebase  = FirebaseFirestore.getInstance();
    class Holder(val view: View):RecyclerView.ViewHolder(view) {
        private lateinit var auth: FirebaseAuth
        fun render(mensajes: Mensajes) {
            auth = FirebaseAuth.getInstance()
            val myMessageLayout: ConstraintLayout = view.findViewById(R.id.myMessageLayout)
            val otherMessage: ConstraintLayout = view.findViewById(R.id.otherMessageLayout)

            val mio:TextView = view.findViewById(R.id.myMessageTextView)
            val other:TextView = view.findViewById(R.id.othersMessageTextView)

            if(mensajes.from.equals(auth.currentUser.email)){
                myMessageLayout.visibility = View.VISIBLE
                otherMessage.visibility = View.GONE
                mio.text = mensajes.message

            }
            else{
                otherMessage.visibility = View.VISIBLE
                myMessageLayout.visibility = View.GONE
                other.text = mensajes.message
            }








        }






    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MessageAdapter.Holder(layoutInflater.inflate(R.layout.message2, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(LISTA[position])
    }

    override fun getItemCount(): Int {
        return LISTA.size
    }

}