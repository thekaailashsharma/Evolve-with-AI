package com.test.palmapi.dto


import com.google.gson.annotations.SerializedName

data class ImageFromText(
    @SerializedName("inputs")
    val inputs: String?
)