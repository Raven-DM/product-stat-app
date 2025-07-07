package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.productanalyticsapp.viewmodel.StatsViewModel
import com.example.productanalyticsapp.R

class ProductStatsFragment : Fragment() {

    private val statsViewModel: StatsViewModel by viewModels()

    private lateinit var textAvgRating: TextView
    private lateinit var textMaxPrice: TextView
    private lateinit var textMinPrice: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_product_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textAvgRating = view.findViewById(R.id.textAvgRating)
        textMaxPrice = view.findViewById(R.id.textMaxPrice)
        textMinPrice = view.findViewById(R.id.textMinPrice)

        statsViewModel.averageRating.observe(viewLifecycleOwner) {
            textAvgRating.text = "Rata-rata Rating: %.2f".format(it)
        }

        statsViewModel.maxPrice.observe(viewLifecycleOwner) {
            textMaxPrice.text = "Harga Maksimum: Rp $it"
        }

        statsViewModel.minPrice.observe(viewLifecycleOwner) {
            textMinPrice.text = "Harga Minimum: Rp $it"
        }

        statsViewModel.calculateStats()
    }
}
