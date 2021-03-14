package com.example.countries.network

import android.util.Log
import com.example.countries.data.BaseResponseData
import com.github.kittinunf.fuel.coroutines.awaitStringResponseResult
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.collections.ArrayList


class NetworkManager {
    suspend fun <ResponseClass : BaseResponseData> getAsync(request: BaseGsonRequest<*>): ArrayList<ResponseClass?> {
        printSendLog(request.url)
        var responeData: ArrayList<ResponseClass?> = ArrayList()
        val (requestSent, response, result) = request.url.httpGet().timeout(30000)
            .awaitStringResponseResult()
        when (result) {
            is Result.Failure -> {
                prinFailLog(request.url)

            }
            is Result.Success -> {
                val collectionType: Type = object : TypeToken<Collection<ResponseClass?>?>() {}.type
                val gson = Gson()
                var myList: List<LinkedTreeMap<String, Any>> = gson.fromJson(
                    result.get(),
                    collectionType
                )
                for (item in myList) {
                    val jsonObject = gson.toJsonTree(item).asJsonObject
                    val obj = gson.fromJson(jsonObject, request.classOfResponse) as ResponseClass
                    responeData.add(obj)
                }
            }
        }
        return responeData
    }


    private fun printSendLog(url: String) {
        Log.d("Request Sent", "Url:" + url)
    }

    private fun prinFailLog(url: String) {
        Log.d("Request Failed", "Url:" + url)
    }

    private fun printSuccessLog(url: String, json: String) {
        val jsonObj = JSONObject(json)
        Log.d("Respone", "Url:" + url + "\n" + jsonObj.toString(jsonObj.length()))
    }
}