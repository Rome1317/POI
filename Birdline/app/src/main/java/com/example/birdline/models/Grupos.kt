package com.example.birdline.models

data class Grupos (
    var id: String = "",
    var name: String = "",
    var users: List<String> = emptyList()
)
{
    class Subgrupos(
        var id: String = "",
        var name: String = "",
        var users: List<String> = emptyList()
    )
}