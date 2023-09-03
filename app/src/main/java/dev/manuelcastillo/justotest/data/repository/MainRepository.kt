package dev.manuelcastillo.justotest.data.repository

import android.util.Log
import dev.manuelcastillo.justotest.data.dto.toDomain
import dev.manuelcastillo.justotest.data.model.RandomUser
import dev.manuelcastillo.justotest.data.network.RandomUserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainRepository {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://randomuser.me/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service: RandomUserService = retrofit.create(RandomUserService::class.java)

    suspend fun getRandomUser() : RandomUser? {
        return try {
            val response = service.getRandomUsers()
            if (response.isSuccessful) {
                response.body()?.users?.first()?.toDomain()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("MainViewModel", e.message.toString())
            null
        }
    }
}