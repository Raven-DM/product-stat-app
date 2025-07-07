package com.example.productstatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productstatapp.R
import com.example.productstatapp.adapters.ProductAdapter
import com.example.productstatapp.databinding.FragmentProductListBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {
    private lateinit var adapter: ProductAdapter
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        adapter = ProductAdapter { id ->
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
        getProductList()
        return binding.root
    }

    private fun getProductList() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    adapter.submitList(response.body()?.products)
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.message}", e)
            }
        }
    }
}