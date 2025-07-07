package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.productstatapp.databinding.FragmentProductStatsBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductStatsFragment : Fragment() {
    private lateinit var binding: FragmentProductStatsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductStatsBinding.inflate(inflater, container, false)
        loadStats()
        return binding.root
    }

    private fun loadStats() {
        lifecycleScope.launch {
            val products = RetrofitClient.api.getProducts().body()?.products ?: emptyList()
            if (products.isNotEmpty()) {
                val ratings = products.map { it.rating }
                val prices = products.map { it.price }
                binding.avgRating.text = "Avg Rating: %.2f".format(ratings.average())
                binding.minPrice.text = "Min Price: %.2f".format(prices.minOrNull() ?: 0.0)
                binding.maxPrice.text = "Max Price: %.2f".format(prices.maxOrNull() ?: 0.0)
            }
        }
    }
}