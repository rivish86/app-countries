package com.example.countries.network

import com.example.countries.data.BaseResponseData

abstract class BaseGsonRequest<ResponseClass : BaseResponseData>(
    val url: String,
    val classOfResponse: Class<ResponseClass>
)