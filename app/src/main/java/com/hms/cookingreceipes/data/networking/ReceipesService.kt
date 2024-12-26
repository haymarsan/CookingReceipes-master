package com.hms.cookingreceipes.data.networking

import com.hms.cookingreceipes.data.model.Blogspot
import retrofit2.Response
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ReceipesService {
    @POST("feeds/posts/default")
    @FormUrlEncoded
    suspend fun getBlospotList(@FieldMap params: Map<String, String>): Response<Blogspot>

}