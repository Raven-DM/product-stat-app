package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.*
import com.example.productanalyticsapp.network.ApiClient
import com.example.productanalyticsapp.utils.ChartUtils
import kotlinx.coroutines.launch

class StatsViewModel : ViewModel() {

    private val _averageRating = MutableLiveData<Double>()
    val averageRating: LiveData<Double> get() = _averageRating

    private val _maxPrice = MutableLiveData<Double>()
    val maxPrice: LiveData<Double> get() = _maxPrice

    private val _minPrice = MutableLiveData<Double>()
    val minPrice: LiveData<Double> get() = _minPrice

    fun calculateStats() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getAllProducts()
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: return@launch
                    val ratings = products.map { it.rating }
                    val prices = products.map { it.price }

                    _averageRating.value = ChartUtils.calculateAverageRating(ratings)
                    _maxPrice.value = ChartUtils.getMaxPrice(prices)
                    _minPrice.value = ChartUtils.getMinPrice(prices)
                }
            } catch (_: Exception) {}
        }
    }
}
