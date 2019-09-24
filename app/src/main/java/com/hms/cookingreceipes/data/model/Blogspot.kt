package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Blogspot(
    @SerializedName("version") val version: String,
    @SerializedName("encoding") val encoding: String,
    @SerializedName("feed") val feed: Feed
) : Serializable



























