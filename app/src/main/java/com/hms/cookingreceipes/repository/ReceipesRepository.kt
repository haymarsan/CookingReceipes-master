package com.hms.cookingreceipes.repository

import android.util.Log
import com.hms.cookingreceipes.data.model.Blogspot
import com.hms.cookingreceipes.data.networking.ReceipesService
import com.hms.cookingreceipes.utils.NetworkResult

class ReceipesRepository(private val receipesService: ReceipesService) {

    suspend fun getBlogSpotArticles(params: Map<String, String>): NetworkResult<Blogspot> =
        try {
            val response = receipesService.getBlospotList(params)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body())
            } else {
                response.errorBody()
                Log.i("Error Response >>>>", response.errorBody().toString())
                Log.i("Error Code >>>>", response.code().toString())
                Log.i("Error Message >>>>", response.message())
                NetworkResult.Error("Something Went Wrong: ${response.message()}")
            }
        } catch (e: Exception) {
            Log.i("Catch Exception>>>>", e.localizedMessage)
            NetworkResult.Error(e.localizedMessage)
        }
}