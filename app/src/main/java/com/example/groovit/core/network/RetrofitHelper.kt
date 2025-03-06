package com.example.groovit.core.network

import com.example.groovit.core.auth.AuthManager
import com.example.groovit.login.data.datasource.LoginService
import com.example.groovit.register.data.datasource.GeneroService
import com.example.groovit.register.data.datasource.RegisterService
import com.example.groovit.home.data.datasource.EventService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "http://3.224.14.28:3000/"

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val token = AuthManager.getToken()

        val request = if (!token.isNullOrEmpty()) {
            original.newBuilder()
                .header("Authorization", token) // Sin el prefijo "Bearer"
                .method(original.method, original.body)
                .build()
        } else {
            original
        }

        chain.proceed(request)
    }

    // Cliente HTTP con el interceptor
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Usa el cliente personalizado
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginService: LoginService by lazy {
        retrofit.create(LoginService::class.java)
    }

    val registerService: RegisterService by lazy {
        retrofit.create(RegisterService::class.java)
    }

    val generoService: GeneroService by lazy {
        retrofit.create(GeneroService::class.java)
    }

    val eventService: EventService by lazy {
        retrofit.create(EventService::class.java)
    }
}