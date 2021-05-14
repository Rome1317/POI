package com.example.birdline.frags

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.birdline.activities.GroupListActicity
import com.example.birdline.R
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
class frag_b : Fragment() {
    lateinit var btnContacto: Button
    lateinit var BtnGrupo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_contacts_list, container, false)
        //BtnGrupo =root.findViewById<Button>(R.id.btnGroups)
        //BtnGrupo.setOnClickListener(){
        //    showGrupos()
        //}
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

}