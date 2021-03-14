package com.example.countries.network.request

import com.example.countries.data.Country
import com.example.countries.network.BaseGsonRequest

class CountriesRequest(urlRequest: String) :
    BaseGsonRequest<Country>(urlRequest, Country::class.java) {
}