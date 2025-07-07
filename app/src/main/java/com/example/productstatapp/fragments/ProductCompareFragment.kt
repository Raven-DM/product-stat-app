package com.example.productstatapp.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.productstatapp.databinding.FragmentProductCompareBinding
import com.example.productstatapp.models.Product
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductCompareFragment : Fragment() {
    private lateinit var binding: FragmentProductCompareBinding
    private var products: List<Product> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductCompareBinding.inflate(inflater, container, false)
        loadProducts()
        return binding.root
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getProducts()
                if (response.isSuccessful) {
                    products = response.body()?.products ?: emptyList()
                    val titles = products.map { it.title }
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, titles)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerProduct1.adapter = adapter
                    binding.spinnerProduct2.adapter = adapter

                    binding.spinnerProduct1.onItemSelectedListener = spinnerListener
                    binding.spinnerProduct2.onItemSelectedListener = spinnerListener
                }
            } catch (e: Exception) {
                Log.e("COMPARE", "Error: ${e.message}")
            }
        }
    }

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val index1 = binding.spinnerProduct1.selectedItemPosition
            val index2 = binding.spinnerProduct2.selectedItemPosition
            if (index1 != index2 && products.isNotEmpty()) {
                val p1 = products[index1]
                val p2 = products[index2]
                val result = "\n${p1.title} vs ${p2.title}\n" +
                        "Price: $${p1.price} vs $${p2.price}\n" +
                        "Rating: ${p1.rating} vs ${p2.rating}\n"
                binding.tvResult.text = result
            } else {
                binding.tvResult.text = "Select two different products."
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}
