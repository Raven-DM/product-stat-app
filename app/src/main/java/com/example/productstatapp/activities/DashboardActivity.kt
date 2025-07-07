package com.example.productstatapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

import androidx.appcompat.app.AppCompatActivity
import com.example.productstatapp.R
import com.example.productstatapp.fragments.BrandFilterFragment
import com.example.productstatapp.fragments.ProductCompareFragment
import com.example.productstatapp.fragments.ProductListFragment
import com.example.productstatapp.fragments.ProductSearchFragment
import com.example.productstatapp.fragments.ProductStatsFragment
import com.example.productstatapp.fragments.RatingChartFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class DashboardActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNav = findViewById(R.id.bottomNavigation)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ProductListFragment())
            .commit()

        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> ProductListFragment()
                R.id.nav_stats -> ProductStatsFragment()
                R.id.nav_search -> ProductSearchFragment()
                R.id.nav_rating -> RatingChartFragment()
                R.id.nav_compare -> ProductCompareFragment()
                else -> null
            }

            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, it)
                    .commit()
            }
            true
        }

        bottomNav.selectedItemId = R.id.nav_home
    }
}
