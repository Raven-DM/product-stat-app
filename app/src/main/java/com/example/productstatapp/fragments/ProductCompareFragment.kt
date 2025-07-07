package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.productstatapp.databinding.FragmentProductCompareBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductCompareFragment : Fragment() {
    private lateinit var binding: FragmentProductCompareBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductCompareBinding.inflate(inflater, container, false)
        binding.btnCompare.setOnClickListener {
            compareProducts()
        }
        return binding.root
    }

    private fun compareProducts() {
        val id1 = binding.etId1.text.toString().toIntOrNull() ?: return
        val id2 = binding.etId2.text.toString().toIntOrNull() ?: return

        lifecycleScope.launch {
            val p1 = RetrofitClient.api.getProductById(id1).body()
            val p2 = RetrofitClient.api.getProductById(id2).body()

            val result = "${p1?.title} (⭐${p1?.rating}) vs ${p2?.title} (⭐${p2?.rating})"
            binding.tvCompareResult.text = result
        }
    }
}