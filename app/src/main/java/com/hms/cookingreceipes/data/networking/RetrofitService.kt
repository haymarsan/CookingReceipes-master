package com.hms.cookingreceipes.data.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService {
    val retrofit=Retrofit.Builder()
        .baseUrl("https://myanmarreceipes.blogspot.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <S> createService(serviceClass: Class<S>): S{
        return retrofit.create(serviceClass)
    }
}

//http://auntysweet.blogspot.com
//http://mmlazp.blogspot.com