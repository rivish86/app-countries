package com.example.countries

import android.app.Application
import com.example.countries.network.NetworkManager

class CountriesApplication  : Application(){

    companion object {
        lateinit var networkManager: NetworkManager
    }

    override fun onCreate() {
        super.onCreate()
        networkManager = NetworkManager()
    }
}