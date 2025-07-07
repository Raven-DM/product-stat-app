package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.productanalyticsapp.viewmodel.FilterViewModel
import com.example.productanalyticsapp.R

class BrandFilterFragment : Fragment() {

    private val filterViewModel: FilterViewModel by activityViewModels()

    private lateinit var spinnerBrand: Spinner
    private lateinit var spinnerCategory: Spinner
    private lateinit var buttonApply: Button

    private val brands = listOf("Samsung", "Apple", "Huawei", "OPPO", "Xiaomi")
    private val categories = listOf("smartphones", "laptops", "fragrances", "skincare", "home-decoration")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_brand_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        spinnerBrand = view.findViewById(R.id.spinnerBrand)
        spinnerCategory = view.findViewById(R.id.spinnerCategory)
        buttonApply = view.findViewById(R.id.buttonApply)

        val brandAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, brands)
        spinnerBrand.adapter = brandAdapter

        val categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = categoryAdapter

        buttonApply.setOnClickListener {
            val selectedBrand = spinnerBrand.selectedItem as String
            val selectedCategory = spinnerCategory.selectedItem as String

            filterViewModel.setBrand(selectedBrand)
            filterViewModel.setCategory(selectedCategory)

            Toast.makeText(requireContext(), "Filter diterapkan", Toast.LENGTH_SHORT).show()
        }
    }
}
