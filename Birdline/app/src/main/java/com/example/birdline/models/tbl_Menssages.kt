package com.poi.camppus.models

import com.google.firebase.database.Exclude
import java.util.*

data class tbl_Menssages (
        var message: String = "",
        var from: String = "",
        var dob: Date = Date()
)



