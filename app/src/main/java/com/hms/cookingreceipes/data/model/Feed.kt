package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Feed(
    @SerializedName("xmlns") val xmlns: String,
    @SerializedName("xmlns\$openSearch") val xmlnsopenSearch: String,
    @SerializedName("xmlns\$blogger") val xmlnsblogger: String,
    @SerializedName("xmlns\$georss") val xmlnsgeorss: String,
    @SerializedName("xmlns\$gd") val xmlnsgd: String,
    @SerializedName("xmlns\$thr") val xmlnsthr: String,
    @SerializedName("id") val value: Value,
    @SerializedName("updated") val updated: Update,
    @SerializedName("category") val category: List<Category>,
    @SerializedName("title") val title: Title,
    @SerializedName("subtitle") val subtitle: Title,
    @SerializedName("link") val link: List<Link>,
    @SerializedName("author") val author: List<Author>,
    @SerializedName("generator") val generator: Generator,
    @SerializedName("openSearch\$totalResults") val openSearchtotalResults: Value,
    @SerializedName("openSearch\$startIndex") val openSearchstartIndex: Value,
    @SerializedName("openSearch\$itemsPerPage") val openSearchitemsPerPage: Value,
    @SerializedName("entry") val entry: List<Entry>
) : Serializable