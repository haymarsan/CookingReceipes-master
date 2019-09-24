package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Author(
    @SerializedName("name") val name: Value,
    @SerializedName("uri") val uri: Value,
    @SerializedName("email") val email: Value,
    @SerializedName("gd\$image") val gdimage: GdImage
) : Serializable