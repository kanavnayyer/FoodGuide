package com.awesome.fithealth.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.awesome.fithealth.R
import com.awesome.fithealth.databinding.FragmentMealDetailBinding
import com.bumptech.glide.Glide

class MealDetailFragment : Fragment() {
    private lateinit var binding: FragmentMealDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve arguments passed from the clicked meal item
        val args = arguments
        val mealName = args?.getString("mealName", "")
        val mealDescription = args?.getString("mealDescription", "")
        val mealImage = args?.getString("mealImage", "")

        // Set the meal details in the UI
        binding.textViewMealName.text = mealName
        binding.textViewMealDescription.text = mealDescription
        Glide.with(requireContext())
            .load(mealImage)
            .into(binding.imageViewMeal)

    }
}
