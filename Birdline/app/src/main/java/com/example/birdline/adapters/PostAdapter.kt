package com.example.birdline.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.R
import com.example.birdline.models.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PostAdapter(val context: Context, var LISTA: List<Post>): RecyclerView.Adapter<PostAdapter.Holder>() {
    val firebase  = FirebaseFirestore.getInstance();

    class Holder(val view: View):RecyclerView.ViewHolder(view) {
        private lateinit var auth: FirebaseAuth
        fun render(posts: Post) {
            auth = FirebaseAuth.getInstance()

            val postLayout : ConstraintLayout = view.findViewById(R.id.postLayout)
            val userPost: TextView = view.findViewById(R.id.userPost)
            val descripcionPost: TextView = view.findViewById(R.id.messagePost)
            val fechaPost: TextView = view.findViewById(R.id.timePost)

            userPost.text = posts.from
            descripcionPost.text = posts.publicacion
            fechaPost.text = posts.dob.toString()

        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostAdapter.Holder(layoutInflater.inflate(R.layout.posts, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.render(LISTA[position])
    }

    override fun getItemCount(): Int {
        return LISTA.size
    }
}