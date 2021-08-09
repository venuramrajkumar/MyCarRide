package com.raj.mycarride.rest.utilretrofit

data class UserResponse(
    val `data`: List<Data>,
    val meta: Meta
)

data class Data(
    val email: String,
    val gender: String,
    val id: Int,
    val name: String,
    val status: String
)
data class Links(
    val current: String,
    val next: String,
    val previous: Any
)

data class Meta(
    val pagination: Pagination
)
data class Pagination(
    val limit: Int,
    val links: Links,
    val page: Int,
    val pages: Int,
    val total: Int
)