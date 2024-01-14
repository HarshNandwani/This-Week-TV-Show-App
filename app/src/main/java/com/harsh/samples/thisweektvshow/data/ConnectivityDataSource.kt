package com.harsh.samples.thisweektvshow.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.net.HttpURLConnection
import java.net.URL


interface ConnectivityDataSource {
    fun isConnected(): Boolean
    fun isInternetAvailable(): Boolean
}

class InternalConnectivityManager(application: Application): ConnectivityDataSource {

    private val connectivityManager: ConnectivityManager = application.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnected(): Boolean {
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    override fun isInternetAvailable(): Boolean {
        if (!isConnected()) return false
        return try {
            val urlConnection = URL("http://www.google.com").openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 300
            urlConnection.connect()
            urlConnection.responseCode == 200
        } catch (_: Exception) {
            false
        }
    }


}
