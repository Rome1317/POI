package com.example.birdline.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.models.Mensajes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class MessageAdapter(val context: Context, var LISTA: List<Mensajes>): RecyclerView.Adapter<MessageAdapter.Holder>(){
    val firebase  = FirebaseFirestore.getInstance();
    class Holder(val view: View):RecyclerView.ViewHolder(view) {
        private lateinit var auth: FirebaseAuth
        @RequiresApi(Build.VERSION_CODES.O)
        fun render(mensajes: Mensajes) {
            auth = FirebaseAuth.getInstance()
            val myMessageLayout: ConstraintLayout = view.findViewById(R.id.myMessageLayout)
            val otherMessage: ConstraintLayout = view.findViewById(R.id.otherMessageLayout)

            val mio:TextView = view.findViewById(R.id.myMessageTextView)
            val other:TextView = view.findViewById(R.id.othersMessageTextView)

            val localDateTime = mensajes.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm:ss")
            val formatted = localDateTime.format(formatter)

            if(mensajes.from.equals(auth.currentUser.email)){
                val user: TextView = view.findViewById(R.id.tvNombre)
                val fechaMensaje: TextView = view.findViewById(R.id.tvHora)

                myMessageLayout.visibility = View.VISIBLE
                otherMessage.visibility = View.GONE
                mio.text = mensajes.message
                user.text = mensajes.from
                fechaMensaje.text = formatted.toString()
                //fechaMensaje.text = mensajes.dob.toString()
            }
            else{
                val user: TextView = view.findViewById(R.id.textView5)
                val fechaMensaje: TextView = view.findViewById(R.id.textView6)

                otherMessage.visibility = View.VISIBLE
                myMessageLayout.visibility = View.GONE
                other.text = mensajes.message
                user.text = mensajes.from
                fechaMensaje.text = formatted.toString()
                //fechaMensaje.text = mensajes.dob.toString()
            }





        }






    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MessageAdapter.Holder(layoutInflater.inflate(R.layout.message, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(LISTA[position])
    }

    override fun getItemCount(): Int {
        return LISTA.size
    }

}