package com.example.countries

import android.R
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.View
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countries.R.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.border_country_popup.*


class BordersDialog(var activity: Activity, internal var adapter: RecyclerView.Adapter<*>, var titleText: String) :
    Dialog(activity), View.OnClickListener {
    var dialog: Dialog? = null
    internal var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layout.border_country_popup)

        title.text = titleText
        recyclerView = countries_rv
        layoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = adapter

        close_btn.setOnClickListener(this)

    }


    override fun onClick(v: View) {
        dismiss()
    }

}