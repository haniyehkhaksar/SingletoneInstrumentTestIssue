package com.example.zephyrtestapp.setuptablet

import android.content.Intent
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.zephyrtestapp.EspressoIdlingResource
import com.example.zephyrtestapp.MyApp
import com.example.zephyrtestapp.test.DaggerTestAppComponent
import com.example.zephyrtestapp.ui.MainActivity
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import okio.IOException
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Rule

open class SharedTabletSetup {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val app: MyApp get() = instrumentation.targetContext.applicationContext as MyApp

//    @Inject
//    lateinit var plugAndPlayInterceptor: PlugAndPlayInterceptor

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true, false)

    @Before
    fun beforeBlock() {
        /**
         * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
         * are not scheduled in the main Looper (for example when executed on a different thread).
         */
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)

        val appInjector = DaggerTestAppComponent.builder()
            .application(app)
            .build()
        appInjector.inject(this)

        val intent = Intent(
            InstrumentationRegistry.getInstrumentation()
                .targetContext, MainActivity::class.java
        )

        activityRule.launchActivity(intent)
    }

    @After
    fun afterBlock() {
        /**
         * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
         */
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
//        plugAndPlayInterceptor.unPlug()
    }

}

class SetupTabletMockInterceptor : Interceptor {

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
                        .contentEquals("correct@test.com".toLowerCase()) ||
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
                            "    \"id\": 5519,\n" +
                            "    \"firstName\": \"Test\",\n" +
                            "    \"lastName\": \"Test\",\n" +
                            "    \"email\": \"correct@test.com\",\n" +
                            "    \"phone\": \"1-111-111-1234\"\n" +
                            "  },\n" +
                            "  \"token\": \"test-pro-token\",\n" +
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
                        "  \"id\": 1111,\n" +
                        "  \"imei\": \"test-imie\"\n" +
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
