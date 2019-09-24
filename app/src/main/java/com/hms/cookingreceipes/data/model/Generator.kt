package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Generator(
    @SerializedName("version") val version: String,
    @SerializedName("uri") val uri: String,
    @SerializedName("\$t") val value: String
) : Serializable