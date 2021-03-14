package com.example.countries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.data.Country
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_view.view.*

class CountriesAdapter(private var countries: MutableList<Country?>,
                       private var listener: AdapterListener?, private var isItemClickEnabled: Boolean)
    : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {


    interface AdapterListener {
        fun onItemClicked(country: Country)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        // Inflating R.layout.item_view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return CountryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        // Getting element from country list at this position
        val countryString = countries[position]!!.name + " " + countries[position]!!.nativeName
        // Updating the text of the txtView with this element
        holder.textView.text = countryString
        if (isItemClickEnabled) {
            holder.itemView.setOnClickListener{listener?.onItemClicked(countries[position]!!)}
        }
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun updateCountriesList(countries: MutableList<Country?>) {
        if (countries != null) {
            this.countries = countries
        }
        notifyDataSetChanged()
    }


    class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.countryString
    }




}