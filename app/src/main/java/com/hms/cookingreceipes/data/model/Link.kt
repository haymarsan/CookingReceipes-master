package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Link(
    @SerializedName("rel") val rel: String,
    @SerializedName("type") val type: String,
    @SerializedName("href") val href: String,
    @SerializedName("title") val title: String
) : Serializable