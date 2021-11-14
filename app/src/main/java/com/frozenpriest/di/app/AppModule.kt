package com.frozenpriest.di.app

import android.app.Application
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.UrlProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule(val application: Application) {

    @AppScope
    @Provides
    fun provideDoctorScheduleApi(retrofit: Retrofit): DoctorScheduleApi {
        return retrofit.create(DoctorScheduleApi::class.java)
    }

    @AppScope
    @Provides
    fun provideRetrofit(urlProvider: UrlProvider, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlProvider.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @AppScope
    @Provides
    fun provideUrlProvider(): UrlProvider {
        return UrlProvider()
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }
}
