package com.hms.cookingreceipes.repository

import com.hms.cookingreceipes.data.model.Blogspot
import com.hms.cookingreceipes.data.networking.ReceipesService

class ReceipesRepository(private val receipesService: ReceipesService) {

    suspend fun getBlogSpotArticles(params: Map<String, String>): Result<Blogspot> =
        try {
            val response = receipesService.getBlospotList(params)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Fail to load data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}