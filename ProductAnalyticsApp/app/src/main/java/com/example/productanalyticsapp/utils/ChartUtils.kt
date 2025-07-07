package com.example.productanalyticsapp.utils

object ChartUtils {
    fun getMaxPrice(prices: List<Double>): Double {
        return prices.maxOrNull() ?: 0.0
    }

    fun getMinPrice(prices: List<Double>): Double {
        return prices.minOrNull() ?: 0.0
    }

    fun calculateAverageRating(ratings: List<Double>): Double {
        return if (ratings.isNotEmpty()) ratings.average() else 0.0
    }
}
