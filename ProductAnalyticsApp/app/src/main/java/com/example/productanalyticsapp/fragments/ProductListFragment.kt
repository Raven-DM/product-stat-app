package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.productanalyticsapp.adapter.ProductAdapter
import com.example.productanalyticsapp.viewmodel.ProductViewModel
import com.example.productanalyticsapp.R

class ProductListFragment : Fragment() {

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var textError: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        progressBar = view.findViewById(R.id.progressBar)
        textError = view.findViewById(R.id.textError)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.products.observe(viewLifecycleOwner) { products ->
            recyclerView.adapter = ProductAdapter(products) { product ->
                // TODO: handle item click (navigate to detail)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            textError.visibility = if (errorMsg != null) View.VISIBLE else View.GONE
            textError.text = errorMsg
        }
    }
}
