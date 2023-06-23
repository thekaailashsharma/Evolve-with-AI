package com.test.palmapi.dto


import com.google.gson.annotations.SerializedName

data class ApiPrompt(
    @SerializedName("prompt")
    val prompt: Prompt?
)