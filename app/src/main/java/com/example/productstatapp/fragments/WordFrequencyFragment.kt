package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.productstatapp.databinding.FragmentWordFrequencyBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class WordFrequencyFragment : Fragment() {
    private lateinit var binding: FragmentWordFrequencyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWordFrequencyBinding.inflate(inflater, container, false)
        analyzeWords()
        return binding.root
    }

    private fun analyzeWords() {
        lifecycleScope.launch {
            val products = RetrofitClient.api.getProducts().body()?.products ?: emptyList()
            val descriptions = products.joinToString(" ") { it.description }
            val words = descriptions.lowercase().split(" ").filter { it.length > 3 }
            val freq = words.groupingBy { it }.eachCount().toList().sortedByDescending { it.second }.take(10)
            binding.textView.text = freq.joinToString("\n") { "${it.first}: ${it.second}" }
        }
    }
}