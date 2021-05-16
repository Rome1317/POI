package  com.example.birdline.models

import java.util.*

data class Mensajes (
    var message: String = "",
    var from: String = "",
    var dob: Date = Date(),
    var encrypted:Boolean = false
)