package com.frozenpriest.di.app

import android.app.Application
import com.frozenpriest.data.remote.AuthInterceptor
import com.frozenpriest.data.remote.DoctorScheduleApi
import com.frozenpriest.data.remote.UrlProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
    fun provideRetrofit(urlProvider: UrlProvider, client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(urlProvider.getBaseUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @AppScope
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @AppScope
    @Provides
    fun provideUrlProvider(): UrlProvider {
        return UrlProvider()
    }

    @AppScope
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @AppScope
    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }
}
