package stepan.gorokhov.music.data.registration.models

import com.google.gson.annotations.SerializedName

data class AuthKey(
    @SerializedName("auth_key")
    val authKey:String
)