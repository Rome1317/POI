package com.example.birdline.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.example.birdline.R

class SubGroupActivity : AppCompatActivity() {

    private lateinit var _post: EditText
    private lateinit var _id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subgroup_post)


        var uid = intent.getStringExtra("ID")
        var SubGroupName = intent.getStringExtra("SubGroup")

        var subgroup: TextView = findViewById(R.id.subgroupname)
        subgroup.text = SubGroupName

        _post = findViewById(R.id.postTextField)
        if (uid != null) {
            _id = uid
        }
    }
}