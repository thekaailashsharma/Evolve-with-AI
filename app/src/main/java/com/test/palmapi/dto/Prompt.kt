package com.test.palmapi.dto


import com.google.gson.annotations.SerializedName

data class Prompt(
    @SerializedName("text")
    val text: String?
)