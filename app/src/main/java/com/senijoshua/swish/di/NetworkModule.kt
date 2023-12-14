package com.senijoshua.swish.di

import com.senijoshua.swish.BuildConfig
import com.senijoshua.swish.data.MainApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://v2.nba.api-sports.io/"
private const val HEADER_API_KEY = "x-rapidapi-key"
private const val HEADER_API_HOST = "x-rapidapi-host"
private const val API_KEY = "e10b042aba6ead2c2389ed973f06c0db"
private const val API_HOST = "v2.nba.api-sports.io"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().apply {
            add(KotlinJsonAdapterFactory())
        }.build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor() = AuthorisationInterceptor()

    @Singleton
    @Provides
    fun provideOkHttp(
        loggingInterceptor: HttpLoggingInterceptor,
        authorisationInterceptor: AuthorisationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            addInterceptor(authorisationInterceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(okHttpClient)
            addConverterFactory(MoshiConverterFactory.create(moshi))
        }.build()
    }

    @Singleton
    @Provides
    fun provideMainApiService(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)
}

class AuthorisationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authorisedRequestBuilder = request.newBuilder()
        
        authorisedRequestBuilder.addHeader(
            HEADER_API_KEY,
            API_KEY
        )
        authorisedRequestBuilder.addHeader(
            HEADER_API_HOST,
            API_HOST
        )
        return chain.proceed(authorisedRequestBuilder.build())
    }
}
