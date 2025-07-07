package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.productstatapp.databinding.FragmentRatingChartBinding
import com.example.productstatapp.network.RetrofitClient
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch

class RatingChartFragment : Fragment() {
    private lateinit var binding: FragmentRatingChartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRatingChartBinding.inflate(inflater, container, false)
        loadChart()
        return binding.root
    }

    private fun loadChart() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    if (products.isEmpty()) {
                        binding.barChart.clear()
                        binding.barChart.invalidate()
                        return@launch
                    }
                    val entries = products.mapIndexed { i, p ->
                        BarEntry(i.toFloat(), p.rating)
                    }
                    val dataSet = BarDataSet(entries, "Product Ratings").apply {
                        color = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
                        valueTextSize = 12f
                    }
                    val barData = BarData(dataSet)
                    binding.barChart.data = barData
                    binding.barChart.xAxis.apply {
                        granularity = 1f
                        setDrawLabels(false)
                    }
                    binding.barChart.axisLeft.axisMinimum = 0f
                    binding.barChart.axisRight.isEnabled = false
                    binding.barChart.description.isEnabled = false
                    binding.barChart.setFitBars(true)
                    binding.barChart.invalidate()
                } else {
                    binding.barChart.clear()
                    binding.barChart.invalidate()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.barChart.clear()
                binding.barChart.invalidate()
            }
        }
    }
}