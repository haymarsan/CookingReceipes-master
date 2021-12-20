package com.hms.cookingreceipes.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AppUpdate(
    @SerializedName("directDownload") val directDownload: String? = "",
    @field:JvmField
    @SerializedName("isForcedUpdate") var isForcedUpdate: Boolean = false,
    @field:JvmField
    @SerializedName("isPlaystoreAvailable") var isPlaystoreAvailable: Boolean = false,
    @SerializedName("name") val name: String = "",
    @SerializedName("playStore") val playStore: String = "",
    @SerializedName("versionCode") val versionCode: Int = 0,
    @SerializedName("title") val title: String? = "",
    @SerializedName("instruction") val instruction: String? = ""
) : Serializable