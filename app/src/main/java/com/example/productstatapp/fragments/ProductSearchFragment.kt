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
    private var brandList: List<String> = listOf("All Brands")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        setupCategoryFilter()

        binding.btnSearch.setOnClickListener {
            val q = binding.etQuery.text.toString()
            search(q)
        }

        return binding.root
    }

    private fun setupBrandFilter(brands: List<String>) {
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brands)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBrand.adapter = spinnerAdapter

        binding.spinnerBrand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                applyFilter(
                    brand = brands[position],
                    query = binding.etQuery.text.toString(),
                    category = binding.spinnerCategory.selectedItem.toString()
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupCategoryFilter() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getCategories()
                if (response.isSuccessful) {
                    val categories = response.body() ?: listOf()
                    val categoryNames = mutableListOf("All Categories")
                    categoryNames.addAll(categories.map { it.name})

                    val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCategory.adapter = spinnerAdapter

                    binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            val selectedCategory = categoryNames[position]
                            if (selectedCategory == "All Categories") {
                                val query = binding.etQuery.text.toString()
                                search(query)
                            } else {
                                getProductsByCategory(selectedCategory)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }
                }
            } catch (e: Exception) {
                Log.e("CategoryFilter", "Failed to fetch categories", e)
            }
        }
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.searchProduct(query)
                if (response.isSuccessful) {
                    allProducts = response.body()?.products ?: emptyList()

                    val brands = allProducts.mapNotNull { it.brand }.toSet().sorted().toMutableList()
                    brands.add(0, "All Brands")
                    brandList = brands
                    setupBrandFilter(brandList)

                    applyFilter(
                        brand = binding.spinnerBrand.selectedItem?.toString(),
                        query = query,
                        category = binding.spinnerCategory.selectedItem?.toString()
                    )
                }
            } catch (e: Exception) {
                Log.e("SearchError", "Failed to search products", e)
            }
        }
    }

    private fun getProductsByCategory(categorySlug: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.api.getProductsByCategory(categorySlug)
                if (response.isSuccessful) {
                    allProducts = response.body()?.products ?: emptyList()

                    val brands = allProducts.mapNotNull { it.brand }.toSet().sorted().toMutableList()
                    brands.add(0, "All Brands")
                    brandList = brands
                    setupBrandFilter(brandList)

                    applyFilter(
                        brand = binding.spinnerBrand.selectedItem?.toString(),
                        query = binding.etQuery.text.toString(),
                        category = categorySlug
                    )
                }
            } catch (e: Exception) {
                Log.e("CategoryProduct", "Failed to fetch products by category", e)
            }
        }
    }

    private fun applyFilter(brand: String?, query: String?, category: String?) {
        val safeQuery = query ?: ""
        val safeBrand = brand ?: "All Brands"
        val safeCategory = category ?: "All Categories"

        val filtered = allProducts.filter { product ->
            val title = product.title ?: ""
            val prodBrand = product.brand ?: ""
            val prodCategory = product.category ?: ""

            val matchQuery = title.contains(safeQuery, ignoreCase = true)
            val matchBrand = safeBrand == "All Brands" || prodBrand.contains(safeBrand, ignoreCase = true)
            val matchCategory = safeCategory == "All Categories" || prodCategory.equals(safeCategory, ignoreCase = true)

            matchQuery && matchBrand && matchCategory
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
