package com.example.birdline.frags

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.birdline.activities.GroupListActicity
import com.example.birdline.R
import com.example.birdline.adapters.ContactsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.birdline.models.ReferenciasFirebase
import com.example.birdline.models.Users
import kotlin.collections.List

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [List.newInstance] factory method to
 * create an instance of this fragment.
 */
class frag_contact_list : Fragment() {
    lateinit var btnContacto: Button
    lateinit var BtnGrupo: Button

    lateinit var auth: FirebaseAuth
    lateinit var MyContacts: List<Users>
    val firebase  = FirebaseFirestore.getInstance()

    private var context2: Context? = null
    private var adapter: ContactsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_contacts_list, container, false)

        val rvContacts: RecyclerView = root.findViewById<RecyclerView>(R.id.contact_list)
        rvContacts.layoutManager = LinearLayoutManager(this.context2!!)

        val userRef = firebase.collection(ReferenciasFirebase.USUARIOS.toString())
        userRef.get()
                .addOnSuccessListener { document ->
                    MyContacts = document.toObjects(Users::class.java)
                    this.adapter = ContactsAdapter(this.context2!!, MyContacts)
                    rvContacts.adapter = this.adapter
                }


        val group: View = root.findViewById(R.id.btnGroups)
        group.setOnClickListener { view ->
            val intent = Intent (getActivity(), GroupListActicity::class.java)
            startActivity(intent)
        }

        return root
    }

    fun showGrupos(){
        val intent: Intent = Intent(activity, GroupListActicity::class.java)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context2 = context
    }

}