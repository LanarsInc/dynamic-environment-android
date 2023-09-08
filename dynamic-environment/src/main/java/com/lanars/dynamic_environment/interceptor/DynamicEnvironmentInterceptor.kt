package com.lanars.dynamic_environment.interceptor

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

const val dynamicEnvironment = "dynamic_env"
const val isCustomEnvironment = "is_custom_env"

class DynamicEnvironmentInterceptor(context: Context) : Interceptor {

    private val sharedPreferences =
        context.getSharedPreferences(dynamicEnvironment, Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (sharedPreferences.getBoolean(isCustomEnvironment, false)) {
            sharedPreferences.getString(dynamicEnvironment, null)?.let {
                requestBuilder.url(it)
                Log.i("DynamicEnvironmentInterceptor", it)
            }
        }

        return chain.proceed(requestBuilder.build())
    }
}