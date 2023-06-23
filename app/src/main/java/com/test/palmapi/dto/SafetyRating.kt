package com.test.palmapi.dto


import com.google.gson.annotations.SerializedName

data class SafetyRating(
    @SerializedName("category")
    val category: String?,
    @SerializedName("probability")
    val probability: String?
)