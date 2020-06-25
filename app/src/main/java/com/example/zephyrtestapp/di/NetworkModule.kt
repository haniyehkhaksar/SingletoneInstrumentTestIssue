package com.example.zephyrtestapp.di

import android.app.Application
import android.content.Context
import com.example.zephyrtestapp.MyApp
import com.example.zephyrtestapp.network.MyApiService
import com.example.zephyrtestapp.network.MyApiServiceCaller
import com.example.zephyrtestapp.network.MyInterceptor
import com.example.zephyrtestapp.network.MyRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    @Provides
    fun provideApplicationContext(application: MyApp): Context {
        return application.applicationContext
    }

    @Provides
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    @Provides
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        return builder.create()
    }

    @Provides
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .addInterceptor(MyInterceptor())
            .build()
    }

    @Provides
    fun provideRetrofit(
        gson: Gson,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://test.com/")
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideIMyRetrofit(retrofit: Retrofit): MyApiService {
        return retrofit.create(MyApiService::class.java)
    }

    @Provides
    fun provideApiServiceCaller(apiService: MyApiService): MyApiServiceCaller {
        return MyApiServiceCaller(apiService)
    }

    @Provides
    @AppScope
    fun provideMyRepository(
        myApiServiceCaller: MyApiServiceCaller
    ): MyRepository {
        return MyRepository(myApiServiceCaller)
    }

}