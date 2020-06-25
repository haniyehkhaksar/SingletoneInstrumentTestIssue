package com.example.zephyrtestapp.network

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.IOException
import org.json.JSONObject

class MyInterceptor : Interceptor {

    private fun bodyToString(request: Request): String {
        return try {
            val copy: Request = request.newBuilder().build()
            val buffer = Buffer()
            if (copy.body == null) return ""
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        val responseBuilder = Response.Builder().request(request)
            .protocol(Protocol.HTTP_1_0)
            .addHeader("content-type", "application/json")
            .message("mocked response for $url")

        return when {
            url.endsWith("api/login") -> {
                val jsonObject = JSONObject(bodyToString(request))

                if (!jsonObject.getString("email").toLowerCase()
                        .contentEquals("alata@zephyrsleep.com".toLowerCase()) ||
                    !jsonObject.getString("password").toLowerCase()
                        .contentEquals("test".toLowerCase())
                ) {
                    responseBuilder.code(403).body(
                        "{ \"password\": { \"code\": \"IncorrectPassword\", \"message\": \"Incorrect password.\", \"params\": [] } }"
                            .toResponseBody("application/json".toMediaTypeOrNull())
                    ).build()
                } else {
                    val response: String = "{\n" +
                            "  \"profile\": {\n" +
                            "    \"id\": 5555,\n" +
                            "    \"firstName\": \"Andrew\",\n" +
                            "    \"lastName\": \"Lata\",\n" +
                            "    \"email\": \"alata@zephyrsleep.com\",\n" +
                            "    \"phone\": \"5-555-555-5555\"\n" +
                            "  },\n" +
                            "  \"token\": \"real-pro-token\",\n" +
                            "  \"role\": \"Admin\",\n" +
                            "  \"availableForCollaboration\": false,\n" +
                            "  \"matrxUnits\": 0\n" +
                            "}"
                    responseBuilder.code(200)
                        .body(response.toResponseBody("application/json".toMediaTypeOrNull()))
                        .build()
                }
            }

            url.endsWith("api-admin/tablets") -> {
                val response: String = "{\n" +
                        "  \"id\": 5555,\n" +
                        "  \"imei\": \"real-imie\"\n" +
                        "}"
                responseBuilder.code(200)
                    .body(response.toResponseBody("application/json".toMediaTypeOrNull()))
                    .build()
            }

            else -> {
                chain.proceed(request)
            }
        }
    }
}
