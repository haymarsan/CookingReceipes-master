package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Entry(
    @SerializedName("id") val id: Value,
    @SerializedName("published") val published: DateValue,
    @SerializedName("updated") val updated: Value,
    @SerializedName("title") val title: Title,
    @SerializedName("content") val content: Content,
    @SerializedName("link") val link: List<Link>,
    @SerializedName("media\$thumbnail") val media: MediaThumbnail
) : Serializable