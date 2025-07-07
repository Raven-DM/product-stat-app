package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.productanalyticsapp.viewmodel.DetailViewModel
import com.example.productanalyticsapp.viewmodel.SharedViewModel
import com.example.productanalyticsapp.R

class ProductDetailFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val detailViewModel: DetailViewModel by lazy { DetailViewModel() }

    private lateinit var imageProduct: ImageView
    private lateinit var textTitle: TextView
    private lateinit var textBrand: TextView
    private lateinit var textPrice: TextView
    private lateinit var textDescription: TextView
    private lateinit var textRating: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imageProduct = view.findViewById(R.id.imageProduct)
        textTitle = view.findViewById(R.id.textTitle)
        textBrand = view.findViewById(R.id.textBrand)
        textPrice = view.findViewById(R.id.textPrice)
        textDescription = view.findViewById(R.id.textDescription)
        textRating = view.findViewById(R.id.textRating)

        sharedViewModel.selectedProduct.observe(viewLifecycleOwner) { product ->
            if (product != null) {
                showProductDetail(product.id)
            } else {
                Toast.makeText(requireContext(), "Produk belum dipilih", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProductDetail(productId: Int) {
        detailViewModel.loadProductById(productId)

        detailViewModel.product.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                textTitle.text = it.title
                textBrand.text = "Brand: ${it.brand}"
                textPrice.text = "Harga: Rp${it.price}"
                textDescription.text = it.description
                textRating.text = "⭐ ${it.rating}"

                Glide.with(requireContext())
                    .load(it.thumbnail)
                    .into(imageProduct)
            }
        })

        detailViewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), "Error: $it", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
