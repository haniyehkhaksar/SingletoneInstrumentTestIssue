package com.example.zephyrtestapp.model

import com.google.gson.annotations.SerializedName

enum class Role(val value: Int) {
    @SerializedName("invalid", alternate = ["Invalid"])
    Invalid(-1),

    @SerializedName("admin", alternate = ["Admin"])
    Admin(0),

    @SerializedName("practitioner", alternate = ["Practitioner"])
    Professional(1);

    companion object {
        fun fromUserAccessType(userAccessType: UserAccessType?): Role {
            return when (userAccessType) {
                UserAccessType.Admin -> Admin
                UserAccessType.Professional -> Professional
                else -> Invalid
            }
        }
    }
}
