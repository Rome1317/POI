package com.example.birdline.models

import java.util.*

class Tasks (
        var id:String = "",
        var title:String = "",
        var description:String = "",
        var points:String = "",
        var score:String = "0",
        var users: List<String> = emptyList(),
        var group:String = ""
){
}