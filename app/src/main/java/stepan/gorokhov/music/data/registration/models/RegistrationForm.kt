package stepan.gorokhov.music.data.registration.models

import com.google.gson.annotations.SerializedName

data class RegistrationForm(
    @SerializedName("login")
    val login:String,
    @SerializedName("password")
    val password:String
)