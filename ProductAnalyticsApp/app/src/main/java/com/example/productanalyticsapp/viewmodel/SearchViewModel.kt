package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.*
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.network.ApiClient
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val _results = MutableLiveData<List<Product>>()
    val results: LiveData<List<Product>> get() = _results

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun searchProducts(query: String) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.searchProducts(query)
                if (response.isSuccessful) {
                    _results.value = response.body()?.products ?: emptyList()
                    _error.value = null
                } else {
                    _error.value = "Tidak ditemukan (${response.code()})"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }
}
