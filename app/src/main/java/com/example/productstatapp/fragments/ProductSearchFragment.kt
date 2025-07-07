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
import com.example.productstatapp.models.Category
import com.example.productstatapp.models.Product
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductSearchFragment : Fragment() {

    private lateinit var binding: FragmentProductSearchBinding
    private lateinit var adapter: ProductAdapter
    private var allProducts: List<Product> = emptyList()
    private var brandList: List<String> = listOf("All Brands")
    private var categorySlugMap: Map<String, String> = mapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductSearchBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupCategoryFilter()

        binding.btnSearch.setOnClickListener {
            val query = binding.etQuery.text.toString()
            applyFilter(
                brand = binding.spinnerBrand.selectedItem?.toString(),
                query = query
            )
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { id ->
            val detailFragment = ProductDetailFragment().apply {
                arguments = Bundle().apply { putInt("id", id) }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupCategoryFilter() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getCategories()
                if (response.isSuccessful) {
                    val categories = response.body() ?: emptyList()

                    val categoryNames = categories.map { it.name }
                    categorySlugMap = categories.associate { it.name to it.slug }

                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCategory.adapter = adapter

                    binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            val selectedName = categoryNames[position]
                            val slug = categorySlugMap[selectedName] ?: return
                            loadProductsByCategory(slug)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
                }
            } catch (e: Exception) {
                Log.e("CategoryFilter", "Failed to fetch categories", e)
            }
        }
    }

    private fun loadProductsByCategory(slug: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getProductsByCategory(slug)
                if (response.isSuccessful) {
                    allProducts = response.body()?.products ?: emptyList()

                    // Update brand filter
                    val brands = allProducts.mapNotNull { it.brand }.toSet().sorted().toMutableList()
                    brands.add(0, "All Brands")
                    brandList = brands
                    setupBrandFilter(brandList)

                    applyFilter(
                        brand = binding.spinnerBrand.selectedItem?.toString(),
                        query = binding.etQuery.text.toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("ProductFetch", "Failed to fetch products by category", e)
            }
        }
    }

    private fun setupBrandFilter(brands: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brands)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBrand.adapter = adapter

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                applyFilter(
                    brand = brands[position],
                    query = binding.etQuery.text.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun applyFilter(brand: String?, query: String?) {
        val safeBrand = brand ?: "All Brands"
        val safeQuery = query ?: ""

        val filtered = allProducts.filter { product ->
            val titleMatch = product.title?.contains(safeQuery, ignoreCase = true) ?: false
            val brandMatch = safeBrand == "All Brands" || product.brand?.contains(safeBrand, ignoreCase = true) == true
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
