package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productstatapp.R
import com.example.productstatapp.adapters.ProductAdapter
import com.example.productstatapp.databinding.FragmentTrendingProductBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class TrendingProductFragment : Fragment() {
    private lateinit var binding: FragmentTrendingProductBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTrendingProductBinding.inflate(inflater, container, false)
        adapter = ProductAdapter{ id ->
            val fragment = ProductDetailFragment().apply {
                arguments = Bundle().apply { putInt("id", id) }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        loadTrending()
        return binding.root
    }

    private fun loadTrending() {
        lifecycleScope.launch {
            val products = RetrofitClient.api.getProducts().body()?.products ?: emptyList()
            val sorted = products.sortedByDescending { it.rating }.take(5)
            adapter.submitList(sorted)
        }
    }
}