package com.example.birdline.frags

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.models.Chat
import com.example.birdline.activities.AddChatActivity
import com.example.birdline.R
import com.example.birdline.adapters.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [List.newInstance] factory method to
 * create an instance of this fragment.
 */
class frag_chat_list : Fragment() {

    private lateinit var auth: FirebaseAuth
    val firebase  = FirebaseFirestore.getInstance();

    lateinit var Mymessages: List<Chat>

    private var context2:Context? = null
    private var adapter: ChatAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_chat_list, container, false)
        auth = FirebaseAuth.getInstance()


        val rvChat:RecyclerView =root.findViewById<RecyclerView>(R.id.listChatsRecyclerView)
        rvChat.layoutManager = LinearLayoutManager(this.context2!!)
        val userRef = firebase.collection(ReferenciasFirebase.USUARIOS.toString()).document(auth.currentUser.email)
        userRef.collection(ReferenciasFirebase.CHATS.toString()).get()
            .addOnSuccessListener { document ->
                Mymessages= document.toObjects(Chat::class.java)
                this.adapter = ChatAdapter(this.context2!!, Mymessages)
                rvChat.adapter = this.adapter
            }


        userRef.collection(ReferenciasFirebase.CHATS.toString()).addSnapshotListener(){
                messages,error ->
            if (error == null){
                messages?.let {Mymessages= it.toObjects(Chat::class.java)
                    this.adapter = ChatAdapter(this.context2!!, Mymessages)
                    rvChat.adapter = this.adapter }

            }
        }

        val addchat: View = root.findViewById(R.id.btnAddChat)
        addchat.setOnClickListener { view ->
            val intent = Intent (getActivity(), AddChatActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context2 = context
    }

}