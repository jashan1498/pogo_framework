package com.example.pogo_framework.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.pogo_framework.R
import com.example.pogo_framework.helpers.SharedPrefsHelper
import com.example.pogo_framework.ui.adapters.HomeViewPagerAdapter
import com.example.pogo_framework.view_models.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var homeViewModel: HomeViewModel? = null
    private var selectedTabType: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SharedPrefsHelper.initPrefs(applicationContext)
        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(HomeViewModel::class.java)
        attachObservers()
        attachListeners()
    }

    private fun attachListeners() {
        view_pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectTab(position)
            }
        })
    }

    private fun attachObservers() {
        homeViewModel?.getPokemonList()?.observe(this, {
            it?.results?.let {
                if (it.isNullOrEmpty().not()) {
                    initUI()
                } else {
                    // todo jashan show error view
                }
            }
        })
    }

    fun handleTabClick(view: View) {
        if (view.tag != null) {
            selectTab(view.tag.toString().toInt())
        }
    }

    private fun selectTab(type: Int) {
        selectedTabType?.unselect()
        when (type) {
            0 -> {
                selectedTabType = explore_tab
            }
            1 -> {
                selectedTabType = community_tab
            }
            2 -> {
                selectedTabType = team_tab
            }
            3 -> {
                selectedTabType = captured_tab
            }
        }

        selectedTabType?.select()
        view_pager.setCurrentItem(type, true)
    }

    private fun initUI() {
        view_pager.adapter = HomeViewPagerAdapter(supportFragmentManager, this)

    }
}

private fun TextView.unselect() {
    this.background = ContextCompat.getDrawable(context, R.drawable.custom_bordered_bg)
}

private fun TextView.select() {
    this.background = ContextCompat.getDrawable(context, R.drawable.custom_bordered_red)
}
