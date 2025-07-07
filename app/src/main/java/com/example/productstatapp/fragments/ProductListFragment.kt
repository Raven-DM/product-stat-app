package com.example.productstatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productstatapp.adapters.ProductAdapter
import com.example.productstatapp.databinding.FragmentProductListBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {
    private lateinit var adapter: ProductAdapter
    private lateinit var binding: FragmentProductListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductListBinding.inflate(inflater, container, false)
        adapter = ProductAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        Log.d("LIFECYCLE", "OnCreateViewProductlist")

        getProductList()

        return binding.root

    }

    private fun getProductList() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    val list = response.body()?.products
                    Log.d("API", "Jumlah produk: ${list?.size}")
                    adapter.submitList(list)
                } else {
                    Log.e("API", "Gagal response: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "Error: ${e.localizedMessage}")
            }
        }
    }
}
