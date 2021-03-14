package com.example.countries

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.data.Country

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    var countries: MutableList<Country?> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    lateinit var countriesAdapter: CountriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.getCountriesData()?.observe(this, CountriesObserver())
        // bind recycler view
        recyclerView = countryList

        // spinner initialization
        val sortByArray = resources.getStringArray(R.array.sortByArray)
        // bind spinner view
        val spinner = sortSpinner
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortByArray)
            var posSelected = -1
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    if (posSelected != position && viewModel.getCountriesList() != null) {
                        posSelected = position

                        // update countries list with sorting
                        countriesAdapter.updateCountriesList(getSortedCountries(posSelected))
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
    }

    private fun getSortedCountries(posSelected: Int): MutableList<Country?> {
        var countriesList = viewModel.getCountriesList()!!
        when (posSelected) {
            0 -> {      // ascending by name
                countriesList.sortBy { it?.name }
            }
            1 -> {      // descending by name
                countriesList.sortByDescending { it?.name }
            }
            2 -> {      // ascending by area size
                countriesList.sortWith(compareBy({ it?.area }, { it?.name }))
            }
            3 -> {      // descending by area size
                countriesList.sortByDescending { it?.area }
            }
        }
        return countriesList
    }

    inner class CountriesObserver : Observer<MutableList<Country?>> {
        override fun onChanged(countriesListChanged: MutableList<Country?>?) {
            if (countriesListChanged != null) {
                for (country in countriesListChanged) {
                    countries.add(country)
                }
                countriesAdapter =
                    CountriesAdapter(countries, listener = AdapterListenerImp(), true)
                recyclerView.adapter = countriesAdapter
                // setting RecyclerView's layout manager equal to LinearLayoutManager
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                viewModel.createCountryCodeMap()
            }
        }
    }

    inner class AdapterListenerImp : CountriesAdapter.AdapterListener {
        override fun onItemClicked(country: Country) {
            // open list of bordering countries
            val borderingCountries = getBorderingCountries(country)
            var dialogTitle: String
            if (borderingCountries.isNotEmpty()) {
                val bordersAdapter = CountriesAdapter(borderingCountries, null, false)
                dialogTitle = resources.getString(R.string.borders_title) + " " + country.name
                var customDialog = BordersDialog(this@MainActivity, bordersAdapter, dialogTitle)

                customDialog.show()
                customDialog.setCanceledOnTouchOutside(false)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No bordering countries for selected country",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        private fun getBorderingCountries(country: Country): MutableList<Country?> {
            var borderCountries: MutableList<Country?> = mutableListOf()
            val countryByCodeList = viewModel.getCountryByCodeList()
            val borders = country.borders
            if (countryByCodeList.isNotEmpty() && borders.isNotEmpty() && borders != null) {
                for (code in borders) borderCountries.add(countryByCodeList.get(code)!!)
            }
            return borderCountries;
        }

    }

}
