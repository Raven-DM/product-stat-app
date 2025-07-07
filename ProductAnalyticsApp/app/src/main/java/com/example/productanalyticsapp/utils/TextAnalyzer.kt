package com.example.productanalyticsapp.utils

object TextAnalyzer {

    fun countWordFrequency(descriptionList: List<String>): Map<String, Int> {
        val wordCount = mutableMapOf<String, Int>()
        descriptionList.forEach { desc ->
            desc.lowercase()
                .replace(Regex("[^a-z\\s]"), "") // Hapus tanda baca
                .split("\\s+".toRegex())
                .forEach { word ->
                    if (word.isNotBlank()) {
                        wordCount[word] = wordCount.getOrDefault(word, 0) + 1
                    }
                }
        }
        return wordCount.toList().sortedByDescending { it.second }.toMap()
    }
}
