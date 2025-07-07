package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.productanalyticsapp.model.Product
import com.example.productanalyticsapp.network.ApiClient
import com.example.productanalyticsapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RatingChartFragment : Fragment(), CoroutineScope {

    private lateinit var barChart: BarChart
    private val job = Job()
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_rating_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        barChart = view.findViewById(R.id.barChartRating)
        loadChartData()
    }

    private fun loadChartData() {
        launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getAllProducts()
                }

                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    drawChart(products)
                }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    private fun drawChart(products: List<Product>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        products.take(10).forEachIndexed { index, product ->
            entries.add(BarEntry(index.toFloat(), product.rating.toFloat()))
            labels.add(product.title.take(10)) // Truncate for clarity
        }

        val dataSet = BarDataSet(entries, "Rating Produk")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        val data = BarData(dataSet)
        data.barWidth = 0.9f

        barChart.apply {
            this.data = data
            description.text = "Top 10 Produk (Rating)"
            setFitBars(true)
            animateY(1000)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.labelRotationAngle = -45f
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            invalidate()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }
}
