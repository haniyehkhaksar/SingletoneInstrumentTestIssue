package com.example.zephyrtestapp.network

import com.example.zephyrtestapp.model.LoginRequest
import com.example.zephyrtestapp.model.LoginResponse
import com.example.zephyrtestapp.model.TabletRequest
import com.example.zephyrtestapp.model.TabletResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface MyApiService {

    @POST("api/login")
    fun loginAsync(@Body loginRequest: LoginRequest): Deferred<LoginResponse>

    @PUT("api-admin/tablets")
    fun setupTabletAsync(
        @Body tabletRequest: TabletRequest,
        @Header("Authorization") token: String
    ): Deferred<TabletResponse>
}