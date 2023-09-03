package dev.manuelcastillo.justotest.data.dto

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import com.google.gson.annotations.SerializedName
import dev.manuelcastillo.justotest.data.model.RandomUser
import java.util.Locale

data class RandomUserDTO (
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val login: Login,
    @SerializedName("dob")
    val birth: Dob,
    val registered: Dob,
    val phone: String,
    val cell: String,
    val picture: Picture,
)

data class Name (
    val first: String,
    val last: String
)

data class Location (
    val street: Street,
    val city: String,
    val country: String,
    val coordinates: Coordinates
)

data class Street (
    val number: Long,
    val name: String
)

data class Coordinates (
    val latitude: String,
    val longitude: String
)

data class Login (
    val username: String
)

data class Dob (
    val date: String
)

data class Picture (
    val large: String
)

fun RandomUserDTO.toDomain() : RandomUser {


    val name = this.name
    val street = this.location.street
    return RandomUser(
        gender = this.gender,
        name = "${name.first} ${name.last}",
        address = "${street.number} ${street.name}, ${this.location.city}",
        country = this.location.country,
        email = this.email,
        username = this.login.username,
        birthDate = getFormattedDate(this.birth.date),
        registerDate = getFormattedDate(this.registered.date),
        phone = this.phone,
        picture = this.picture.large,
        locationLat = this.location.coordinates.latitude.toDouble(),
        locationLng = this.location.coordinates.longitude.toDouble()
    )
}

private fun getFormattedDate(date: String) : String{
    val calendar = Calendar.getInstance()
    calendar.time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(date)
    return SimpleDateFormat(
        "dd 'de' MMMM 'de' yyyy",
        Locale("es", "ES")
    ).format(calendar.time)
}