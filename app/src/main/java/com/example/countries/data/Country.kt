package com.example.countries.data

data class Country(
    var name: String,
    var alpha3Code: String,
    var area: Float ,
    var borders: ArrayList<String>,
    var nativeName: String
) : BaseResponseData()