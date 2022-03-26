package com.example.helsinkimap.di.module

import com.example.helsinkimap.BuildConfig
import com.example.helsinkimap.data.network.api.NetworkApi
import com.example.helsinkimap.data.network.interceptor.AcceptInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        okHttpClientBuilder: OkHttpClient.Builder,
        acceptInterceptor: AcceptInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient =
        okHttpClientBuilder
            .addInterceptor(acceptInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideRetrofitBuilder(
        moshi: Moshi,
    ): Retrofit.Builder =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

    @Singleton
    @Provides
    fun providePublicRetrofit(
        retrofitBuilder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): Retrofit = retrofitBuilder
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideNetworkApi(
        retrofit: Retrofit,
    ): NetworkApi = retrofit
        .create(NetworkApi::class.java)

    companion object {
        private const val TIMEOUT = 60L
    }
}
