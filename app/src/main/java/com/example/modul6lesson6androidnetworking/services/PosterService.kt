package com.example.modul6lesson6androidnetworking.services

import com.example.modul6lesson6androidnetworking.model.Post
import com.example.modul6lesson6androidnetworking.model.PostResponse
import retrofit2.Call
import retrofit2.http.*

interface PosterService {
    @Headers(
        "Content-type:application/json"
    )

    @GET("posts")
    fun listPost(): Call<ArrayList<PostResponse>>

    @GET("posts/{id}")
    fun singlePost(@Path("id") id : Int): Call<PostResponse>

    @POST("posts")
    fun createPost(@Body post: Post) : Call<PostResponse>

    @PUT("posts/{id}")
    fun updatePost(@Path("id") id : Int, @Body post : Post) : Call<PostResponse>

    @PUT("posts/{id}")
    fun deletePost(@Path("id") id : Int) : Call<PostResponse>
}