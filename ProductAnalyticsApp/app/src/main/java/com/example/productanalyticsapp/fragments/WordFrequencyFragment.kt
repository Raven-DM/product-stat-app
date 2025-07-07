package com.example.productanalyticsapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.productanalyticsapp.viewmodel.WordFrequencyViewModel
import com.example.productanalyticsapp.R

class WordFrequencyFragment : Fragment() {

    private val wordFrequencyViewModel: WordFrequencyViewModel by viewModels()
    private lateinit var containerWords: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_word_frequency, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        containerWords = view.findViewById(R.id.containerWords)

        wordFrequencyViewModel.wordCountMap.observe(viewLifecycleOwner) { wordMap ->
            containerWords.removeAllViews()

            wordMap.entries.take(20).forEach { (word, count) ->
                val tv = TextView(requireContext())
                tv.text = "$word : $count"
                tv.setPadding(8)
                tv.textSize = 16f
                containerWords.addView(tv)
            }
        }

        wordFrequencyViewModel.analyzeDescriptions()
    }
}
