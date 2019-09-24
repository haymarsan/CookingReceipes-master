package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GdImage(
    @SerializedName("rel") val rel: String,
    @SerializedName("width") val width: String,
    @SerializedName("height") val height: String,
    @SerializedName("src") val src: String
) : Serializable