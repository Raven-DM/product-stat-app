package com.example.productstatapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productstatapp.R
import com.example.productstatapp.adapters.ProductAdapter
import com.example.productstatapp.databinding.FragmentProductSearchBinding
import com.example.productstatapp.models.Product
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductSearchFragment : Fragment() {
    private lateinit var binding: FragmentProductSearchBinding
    private lateinit var adapter: ProductAdapter
    private var allProducts: List<Product> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductSearchBinding.inflate(inflater, container, false)

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

        setupBrandFilter() // ⬅️ Tambahkan spinner filter

        val query = arguments?.getString("query")
        query?.let {
            binding.etQuery.setText(it)
            search(it)
        }

        binding.btnSearch.setOnClickListener {
            val q = binding.etQuery.text.toString()
            search(q)
        }

        return binding.root
    }

    private fun setupBrandFilter() {
        val brands = listOf("All Brands", "Apple", "Dior", "Dolce", "Gucci", "Annibale", "Knoll", "Wooden", "Chanel", "Beef") // Ubah sesuai data kamu

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brands)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBrand.adapter = spinnerAdapter

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedBrand = brands[position]
                applyFilter(selectedBrand, binding.etQuery.text.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            val response = RetrofitClient.api.searchProduct(query)
            if (response.isSuccessful) {
                allProducts = response.body()?.products ?: emptyList()
                applyFilter(binding.spinnerBrand.selectedItem.toString(), query)
            }
        }
    }

    private fun applyFilter(brand: String?, query: String?) {
        val safeQuery = query ?: ""

        val filtered = allProducts.filter { product ->
            val titleMatch = product.title?.contains(safeQuery, ignoreCase = true) ?: false
            val brandMatch = brand == "All Brands" || (product.brand?.contains(brand ?: "", ignoreCase = true) ?: false)
            titleMatch && brandMatch
        }

        adapter.submitList(filtered)
    }


    companion object {
        fun newInstance(query: String): ProductSearchFragment {
            val fragment = ProductSearchFragment()
            val args = Bundle()
            args.putString("query", query)
            fragment.arguments = args
            return fragment
        }
    }
}