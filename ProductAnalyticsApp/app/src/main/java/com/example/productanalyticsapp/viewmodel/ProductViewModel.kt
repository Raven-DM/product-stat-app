package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.network.ApiClient
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        fetchAllProducts()
    }

    fun fetchAllProducts() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getAllProducts()
                if (response.isSuccessful) {
                    _products.value = response.body()?.products ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Gagal mengambil data: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
