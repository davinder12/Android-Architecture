package com.android.savery.di

import android.content.Context
import com.android.savery.BuildConfig
import com.android.savery.data.api.AuthApi
import com.sdi.joyersmajorplatform.common.livedataext.mutableLiveData
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val API_VERSION_HEADER = "Accept: application/json; version="

@Module
class ApiModule {


    @Singleton
    @Provides
    fun provideRetrofit(@Named("api_url") baseUrl: String,
                        httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }


    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return buildApiClient(retrofit, AuthApi.MODULE_PATH).create(AuthApi::class.java)
    }
//
//
//    @Singleton
//    @Provides
//    fun provideFileUploadApi(retrofit: Retrofit): FileUploadApi {
//        return buildApiClient(retrofit, FileUploadApi.MODULE_PATH).create(FileUploadApi::class.java)
//    }
//
//
//
//    @Singleton
//    @Provides
//    fun provideCreateJsonData(retrofit: Retrofit): JoyApi {
//        return buildApiClient(retrofit, JoyApi.MODULE_PATH).create(JoyApi::class.java)
//    }




    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        context: Context
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder().apply {
            readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
        }

        httpClient.addNetworkInterceptor(loggingInterceptor)
        return httpClient.build()
    }





    @Singleton
    @Provides
    @Named("api_url")
    fun provideApiUrl(): String {
        return BuildConfig.API_URL
    }

    private fun buildApiClient(retrofit: Retrofit, path: String): Retrofit {
        return retrofit.newBuilder().baseUrl(retrofit.baseUrl().url().toString() + path)
            .validateEagerly(true).build()
    }

    companion object {
        private const val HTTP_TIMEOUT = 40L
    }



    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
    }
}