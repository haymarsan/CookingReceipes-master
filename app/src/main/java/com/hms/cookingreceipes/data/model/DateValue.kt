package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class DateValue(
    @SerializedName("\$t") val value: Date
) : Serializable