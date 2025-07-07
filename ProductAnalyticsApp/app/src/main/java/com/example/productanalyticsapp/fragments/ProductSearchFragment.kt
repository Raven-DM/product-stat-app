package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productanalyticsapp.adapter.ProductAdapter
import com.example.productanalyticsapp.viewmodel.SearchViewModel
import com.example.productanalyticsapp.viewmodel.SharedViewModel
import com.example.productanalyticsapp.R

class ProductSearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var inputSearch: EditText
    private lateinit var buttonSearch: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var textError: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inputSearch = view.findViewById(R.id.inputSearch)
        buttonSearch = view.findViewById(R.id.buttonSearch)
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.recyclerViewResults)
        textError = view.findViewById(R.id.textError)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        buttonSearch.setOnClickListener {
            val keyword = inputSearch.text.toString().trim()
            if (keyword.isNotEmpty()) {
                searchViewModel.searchProducts(keyword)
            }
        }

        searchViewModel.results.observe(viewLifecycleOwner) { products ->
            recyclerView.adapter = ProductAdapter(products) { product ->
                sharedViewModel.selectProduct(product)
                // TODO: navigate to detail fragment
            }
        }

        searchViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        searchViewModel.error.observe(viewLifecycleOwner) { error ->
            textError.visibility = if (error != null) View.VISIBLE else View.GONE
            textError.text = error
        }
    }
}
