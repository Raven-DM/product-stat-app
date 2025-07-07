package com.example.productstatapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.productstatapp.R
import com.example.productstatapp.fragments.ProductDetailFragment
import com.example.productstatapp.models.Product

class ProductAdapter(private val onClick: (Int) -> Unit) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    private val products = mutableListOf<Product>()

    fun submitList(newList: List<Product>?) {
        products.clear()
        if (newList != null) products.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            itemView.findViewById<TextView>(R.id.tvTitle).text = product.title
            itemView.findViewById<TextView>(R.id.tvPrice).text = "$${product.price}"
            itemView.findViewById<TextView>(R.id.tvRating).text = product.rating.toString()
            val iv = itemView.findViewById<ImageView>(R.id.ivProduct)
            Glide.with(itemView).load(product.thumbnail).into(iv)
            itemView.setOnClickListener {
                val bundle = Bundle().apply { putInt("id", product.id) }
                val fragment = ProductDetailFragment().apply { arguments = bundle }
                (itemView.context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}