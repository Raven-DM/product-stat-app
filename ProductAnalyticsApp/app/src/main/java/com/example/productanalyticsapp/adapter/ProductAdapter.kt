package com.example.productanalyticsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.R

class ProductAdapter(
    private val productList: List<Product>,
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageThumbnail: ImageView = view.findViewById(R.id.imageThumbnail)
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val textPrice: TextView = view.findViewById(R.id.textPrice)
        val textRating: TextView = view.findViewById(R.id.textRating)

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(productList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.textTitle.text = product.title
        holder.textPrice.text = "Rp${product.price}"
        holder.textRating.text = "⭐ ${product.rating}"

        Glide.with(holder.itemView.context)
            .load(product.thumbnail)
            .into(holder.imageThumbnail)
    }
}
