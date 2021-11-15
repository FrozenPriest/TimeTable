package com.frozenpriest.data.remote

import com.frozenpriest.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().apply {
            addHeader(Constants.HEADER_APP_ID, Constants.APP_ID)
            addHeader(Constants.HEADER_API_KEY, Constants.API_KEY)
            addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE)

//            addHeader(Constants.HEADER_API_KEY, Constants.API_KEY)
//            addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CONTENT_TYPE)
        }
        return chain.proceed(request.build())
    }
}
