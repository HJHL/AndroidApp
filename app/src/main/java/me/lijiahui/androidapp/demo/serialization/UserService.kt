package me.lijiahui.androidapp.demo.serialization

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/api/users?page={pageId}")
    fun getUsers(@Path("pageId") pageId: Int): Call<ResponseBody>

}