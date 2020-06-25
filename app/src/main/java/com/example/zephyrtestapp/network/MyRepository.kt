package com.example.zephyrtestapp.network

import com.example.zephyrtestapp.model.LoginResponse
import com.example.zephyrtestapp.model.TabletResponse
import javax.inject.Inject

class MyRepository @Inject constructor(private val apiServiceCaller: MyApiServiceCaller) {

    suspend fun login(email: String, password: String): LoginResponse {
        return apiServiceCaller.login(email, password)
    }

    suspend fun setupTablet(imei: String, password: String = ""): TabletResponse {
        return apiServiceCaller.setupTablet(imei, password)
    }
}