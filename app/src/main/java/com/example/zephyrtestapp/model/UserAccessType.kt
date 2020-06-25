package com.example.zephyrtestapp.model

import com.google.gson.annotations.SerializedName

enum class UserAccessType {
    @SerializedName("professional", alternate = ["Professional"])
    Professional,

    @SerializedName("admin", alternate = ["Admin"])
    Admin
}
