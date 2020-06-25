package com.example.zephyrtestapp.di

import android.app.Application
import android.content.Context
import com.example.zephyrtestapp.network.MyApiService
import com.example.zephyrtestapp.network.MyApiServiceCaller
import com.example.zephyrtestapp.network.MyRepository
import com.example.zephyrtestapp.network.PlugAndPlayInterceptor
import com.example.zephyrtestapp.test.TestApp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object MockNetworkModule {

    @Provides
    fun provideApplicationContext(application: TestApp): Context {
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
    fun provideOkHttpClient(interceptors: Array<Interceptor>): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)

        interceptors.forEach {
            builder.addInterceptor(it)
        }

        return builder.build()
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
    fun provideInterceptors(plugAndPlayInterceptor: PlugAndPlayInterceptor): Array<Interceptor> {
        return arrayOf(plugAndPlayInterceptor)
    }


    @Provides
    @AppScope
    fun providePlugAndPlayInterceptor(): PlugAndPlayInterceptor {
        return PlugAndPlayInterceptor()
    }

    @Provides
    @AppScope
    fun provideIMyRetrofit(retrofit: Retrofit): MyApiService {
        return retrofit.create(MyApiService::class.java)
    }

    @Provides
    @AppScope
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