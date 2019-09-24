package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Title(
    @SerializedName("type") val type: String,
    @SerializedName("\$t") val value: String
) : Serializable