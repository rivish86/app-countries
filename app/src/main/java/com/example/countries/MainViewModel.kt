package com.example.countries

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.countries.data.Country
import com.example.countries.network.request.CountriesRequest
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private var countriesDataList: MutableLiveData<MutableList<Country?>>? = null
    private var countryByCodeList = hashMapOf<String, Country>()

    companion object {
        const val COUNTRIES_URL =
            "https://restcountries.eu/rest/v2/all?fields=name;nativeName;alpha3Code;area;borders\n"
    }

    fun getCountriesData(): MutableLiveData<MutableList<Country?>>? {
        when (countriesDataList) {
            null -> {
                countriesDataList = MutableLiveData()
            }
        }
        // call countries api with filter
        viewModelScope.launch {
            getCountriesFromApi()
        }

        return countriesDataList
    }

    private suspend fun getCountriesFromApi() {
        var countries: ArrayList<Country?>
        countries = CountriesApplication.networkManager.getAsync(CountriesRequest(COUNTRIES_URL))
        countriesDataList?.postValue(countries)
    }

    // hash countries by their alpha3Code
    fun createCountryCodeMap() {
        for (country in countriesDataList?.value!!) {
            if (country != null) {
                countryByCodeList.put(country.alpha3Code, country)
            }
        }
    }

    // returns the hash countries
    fun getCountryByCodeList(): HashMap<String, Country> {
        return countryByCodeList
    }

    // returns countries list not as liveData
    fun getCountriesList(): MutableList<Country?>? {
        return countriesDataList?.value
    }


}