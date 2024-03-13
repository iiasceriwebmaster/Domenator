package md.webmasterstudio.domenator.networking

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor() {
    val userService: UserService

    companion object {
        const val BASE_URL: String = "https://domenator.webfun.cf"
//        const val BASE_URL: String = "https://deafinfocenter.webfun.cf"
        var instance: ApiClient? = null
            get() {
                Log.d("CREATION", "ApiClient -> getInstance() ")
                if (field == null) {
                    field = ApiClient()
                }
                return field
            }
            private set
    }

    init {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val retrofit = Retrofit.Builder().addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        ).baseUrl(
            BASE_URL
        ).client(OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()).build()

        this.userService = retrofit.create(
            UserService::class.java
        ) as UserService
    }


}