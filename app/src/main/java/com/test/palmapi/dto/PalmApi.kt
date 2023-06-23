package com.test.palmapi.dto


import com.google.gson.annotations.SerializedName

data class PalmApi(
    @SerializedName("candidates")
    val candidates: List<Candidate?>?
)