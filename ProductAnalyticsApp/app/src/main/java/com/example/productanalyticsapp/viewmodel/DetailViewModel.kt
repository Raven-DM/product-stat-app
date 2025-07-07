package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.*
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.network.ApiClient
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {

    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> get() = _product

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun loadProductById(id: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getProductById(id)
                if (response.isSuccessful) {
                    _product.value = response.body()
                    _error.value = null
                } else {
                    _error.value = "Data tidak ditemukan"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }
}
