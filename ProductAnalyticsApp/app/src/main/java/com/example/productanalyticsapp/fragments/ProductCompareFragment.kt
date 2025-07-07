package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import android.widget.ImageView
import android.view.ViewGroup.LayoutParams
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.viewmodel.SharedViewModel
import com.example.productanalyticsapp.R

class ProductCompareFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var containerCompare: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product_compare, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerCompare = view.findViewById(R.id.containerCompare)

        sharedViewModel.compareList.observe(viewLifecycleOwner) { productList ->
            displayComparison(productList)
        }
    }

    private fun displayComparison(products: List<Product>) {
        containerCompare.removeAllViews()

        for (product in products) {
            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            }

            val image = ImageView(requireContext()).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 300)
                scaleType = ImageView.ScaleType.CENTER_CROP
            }

            Glide.with(this).load(product.thumbnail).into(image)

            val title = TextView(requireContext()).apply {
                text = product.title
                textSize = 18f
                setPadding(0, 8, 0, 4)
            }

            val brand = TextView(requireContext()).apply {
                text = "Brand: ${product.brand}"
            }

            val price = TextView(requireContext()).apply {
                text = "Harga: Rp${product.price}"
            }

            val rating = TextView(requireContext()).apply {
                text = "Rating: ${product.rating}"
            }

            itemLayout.addView(image)
            itemLayout.addView(title)
            itemLayout.addView(brand)
            itemLayout.addView(price)
            itemLayout.addView(rating)

            containerCompare.addView(itemLayout)
        }
    }
}
