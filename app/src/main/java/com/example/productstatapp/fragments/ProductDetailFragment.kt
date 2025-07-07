package com.example.productstatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.productstatapp.R
import com.example.productstatapp.databinding.FragmentProductDetailBinding
import com.example.productstatapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {
    private lateinit var binding: FragmentProductDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        val id = arguments?.getInt("id") ?: return binding.root
        loadDetail(id)
        return binding.root
    }

    private fun loadDetail(id: Int) {
        lifecycleScope.launch {
            val response = RetrofitClient.api.getProductById(id)
            val product = response.body() ?: return@launch
            binding.title.text = product.title
            binding.description.text = product.description
            binding.price.text = getString(R.string.price_format, product.price)
            binding.rating.text = getString(R.string.rating_format, product.rating)
            Glide.with(binding.imageView).load(product.thumbnail).into(binding.imageView)
        }
    }
}
