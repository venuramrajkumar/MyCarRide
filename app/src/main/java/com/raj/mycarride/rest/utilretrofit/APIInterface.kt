package com.raj.mycarride.rest.utilretrofit

import retrofit2.Call
import retrofit2.http.POST

interface APIInterface {

    @POST("/public/v1/users")
    fun getUsers() : Call<UserResponse>

}