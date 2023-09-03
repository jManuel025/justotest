package dev.manuelcastillo.justotest.data.dto

import com.google.gson.annotations.SerializedName

data class RandomUserResponse (
    @SerializedName("results")
    val users: List<RandomUserDTO>,
)

