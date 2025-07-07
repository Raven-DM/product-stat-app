package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productstatapp.adapters.ProductAdapter
import com.example.productstatapp.databinding.FragmentBrandFilterBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class BrandFilterFragment : Fragment() {
    private lateinit var binding: FragmentBrandFilterBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBrandFilterBinding.inflate(inflater, container, false)
        adapter = ProductAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.btnFilter.setOnClickListener {
            val brand = binding.etBrand.text.toString()
            filterByBrand(brand)
        }
        return binding.root
    }

    private fun filterByBrand(brand: String) {
        lifecycleScope.launch {
            val response = RetrofitClient.api.getProducts()
            if (response.isSuccessful) {
                val products = response.body()?.products ?: emptyList()
                val filtered = products.filter { it.brand.contains(brand, ignoreCase = true) }
                adapter.submitList(filtered)
            }
        }
    }
}