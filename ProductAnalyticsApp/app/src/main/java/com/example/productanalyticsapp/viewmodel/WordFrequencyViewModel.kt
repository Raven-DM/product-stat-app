package com.example.productanalyticsapp.viewmodel

import androidx.lifecycle.*
import com.example.productanalyticsapp.network.ApiClient
import com.example.productanalyticsapp.utils.TextAnalyzer
import kotlinx.coroutines.launch

class WordFrequencyViewModel : ViewModel() {

    private val _wordCountMap = MutableLiveData<Map<String, Int>>()
    val wordCountMap: LiveData<Map<String, Int>> get() = _wordCountMap

    fun analyzeDescriptions() {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.getAllProducts()
                if (response.isSuccessful) {
                    val descriptions = response.body()?.products?.map { it.description } ?: emptyList()
                    _wordCountMap.value = TextAnalyzer.countWordFrequency(descriptions)
                }
            } catch (_: Exception) {}
        }
    }
}
