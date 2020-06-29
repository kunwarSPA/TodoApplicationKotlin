package com.chase.kotlincoroutines.network

import com.chase.kotlincoroutines.model.Data
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitService {
    @GET("/todos")
    suspend fun getPosts(): Response<List<Data>>
}