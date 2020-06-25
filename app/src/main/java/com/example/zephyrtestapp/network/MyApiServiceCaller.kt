package com.example.zephyrtestapp.network

import com.example.zephyrtestapp.model.LoginRequest
import com.example.zephyrtestapp.model.LoginResponse
import com.example.zephyrtestapp.model.TabletRequest
import com.example.zephyrtestapp.model.TabletResponse
import javax.inject.Inject

class MyApiServiceCaller @Inject constructor(private val myApiServiceApi: MyApiService) {

    suspend fun login(email: String, password: String): LoginResponse {
        return myApiServiceApi.loginAsync(LoginRequest(email, password)).await()
    }


    suspend fun setupTablet(imei: String, password: String): TabletResponse {
        return myApiServiceApi.setupTabletAsync(TabletRequest(imei), "ProAuth").await()
    }
}