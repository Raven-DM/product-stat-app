package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.productanalyticsapp.model.Product

class SharedViewModel : ViewModel() {

    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> get() = _selectedProduct

    // Internal MutableLiveData dengan MutableList untuk modifikasi
    private val _compareList = MutableLiveData<MutableList<Product>>(mutableListOf())
    // Ekspose LiveData dengan List<Product> agar tidak bisa dimodifikasi dari luar
    val compareList: MutableLiveData<MutableList<Product>> get() = _compareList

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun clearSelected() {
        _selectedProduct.value = null
    }

    fun addToCompare(product: Product) {
        val currentList = _compareList.value ?: mutableListOf()
        if (!currentList.contains(product)) {
            currentList.add(product)
            _compareList.value = currentList
        }
    }

    fun removeFromCompare(product: Product) {
        val currentList = _compareList.value ?: mutableListOf()
        if (currentList.remove(product)) {
            _compareList.value = currentList
        }
    }

    fun clearCompare() {
        _compareList.value = mutableListOf()
    }
}
