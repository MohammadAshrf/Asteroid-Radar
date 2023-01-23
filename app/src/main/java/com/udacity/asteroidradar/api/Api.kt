package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object Api {
    interface ApiService {
        @GET("planetary/apod")
        suspend fun getPictureOfDay(): Response<PictureOfDay>

        @GET("neo/rest/v1/feed")
        suspend fun getAsteroids(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String
        ): Response<String>

    }
}

object Network {

    private var client: OkHttpClient? = null

    private fun logger(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private fun client(): OkHttpClient {
        if (client == null) {
            client = OkHttpClient.Builder()
                .addInterceptor(ApiKeyInterceptor())
                .addInterceptor(logger())
                .build()
        }
        return client as OkHttpClient
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(client())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val asteroidService: Api.ApiService = retrofit.create(Api.ApiService::class.java)
}


class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val chainRequest = chain.request()
        val httpUrl = chainRequest.url
        val url = httpUrl.newBuilder().addQueryParameter("api_key", Constants.API_KEY).build()
        val requestBuilder = chainRequest.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}
