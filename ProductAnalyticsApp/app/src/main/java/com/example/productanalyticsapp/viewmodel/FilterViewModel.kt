package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {

    private val _selectedBrand = MutableLiveData<String?>()
    val selectedBrand: LiveData<String?> get() = _selectedBrand

    private val _selectedCategory = MutableLiveData<String?>()
    val selectedCategory: LiveData<String?> get() = _selectedCategory

    fun setBrand(brand: String?) {
        _selectedBrand.value = brand
    }

    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun clearFilters() {
        _selectedBrand.value = null
        _selectedCategory.value = null
    }
}
