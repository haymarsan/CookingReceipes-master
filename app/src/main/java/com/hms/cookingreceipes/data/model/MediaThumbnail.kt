package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MediaThumbnail(
    @SerializedName("xmlns\$media") val media: String,
    @SerializedName("url") val url: String,
    @SerializedName("height") val height: String,
    @SerializedName("width") val width: String
): Serializable