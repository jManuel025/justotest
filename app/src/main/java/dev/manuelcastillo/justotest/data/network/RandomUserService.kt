package dev.manuelcastillo.justotest.data.network

import dev.manuelcastillo.justotest.data.dto.RandomUserResponse
import retrofit2.Response
import retrofit2.http.GET

interface RandomUserService {
    @GET("api")
    suspend fun getRandomUsers() : Response<RandomUserResponse>
}