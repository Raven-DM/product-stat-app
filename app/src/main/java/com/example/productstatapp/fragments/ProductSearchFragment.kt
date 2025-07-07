package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productstatapp.adapters.ProductAdapter
import com.example.productstatapp.databinding.FragmentProductSearchBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductSearchFragment : Fragment() {
    private lateinit var binding: FragmentProductSearchBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductSearchBinding.inflate(inflater, container, false)
        adapter = ProductAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        binding.btnSearch.setOnClickListener {
            val q = binding.etQuery.text.toString()
            search(q)
        }
        return binding.root
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            val response = RetrofitClient.api.searchProduct(query)
            if (response.isSuccessful) {
                adapter.submitList(response.body()?.products)
            }
        }
    }
}