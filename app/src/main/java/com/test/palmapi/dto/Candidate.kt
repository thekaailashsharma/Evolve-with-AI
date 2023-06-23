package com.test.palmapi.dto


import com.google.gson.annotations.SerializedName

data class Candidate(
    @SerializedName("output")
    val output: String?,
    @SerializedName("safetyRatings")
    val safetyRatings: List<SafetyRating?>?
)