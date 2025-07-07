package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productanalyticsapp.adapter.ProductAdapter
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.network.ApiClient
import com.example.productanalyticsapp.viewmodel.FilterViewModel
import com.example.productanalyticsapp.viewmodel.SharedViewModel
import com.example.productanalyticsapp.R

import kotlinx.coroutines.launch

class TrendingProductFragment : Fragment() {

    private val filterViewModel: FilterViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_trending_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerViewTrending)
        progressBar = view.findViewById(R.id.progressTrending)
        textInfo = view.findViewById(R.id.textTrendingInfo)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadTrendingProducts()
    }

    private fun loadTrendingProducts() {
        progressBar.visibility = View.VISIBLE
        textInfo.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getAllProducts()
                if (response.isSuccessful) {
                    val allProducts = response.body()?.products ?: emptyList()
                    val filtered = filterTrendingProducts(allProducts)
                    recyclerView.adapter = ProductAdapter(filtered) { product ->
                        sharedViewModel.selectProduct(product)
                        // TODO: navigate to detail
                    }

                    textInfo.text = "Menampilkan ${filtered.size} produk trending"
                } else {
                    textInfo.text = "Gagal mengambil data"
                }
            } catch (e: Exception) {
                textInfo.text = "Error: ${e.localizedMessage}"
            } finally {
                progressBar.visibility = View.GONE
                textInfo.visibility = View.VISIBLE
            }
        }
    }

    private fun filterTrendingProducts(products: List<Product>): List<Product> {
        val brand = filterViewModel.selectedBrand.value
        val category = filterViewModel.selectedCategory.value

        return products
            .filter { product ->
                (brand == null || product.brand.equals(brand, true)) &&
                        (category == null || product.category.equals(category, true))
            }
            .sortedByDescending { it.rating }
            .take(10)
    }
}
