package com.awesome.fithealth.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.awesome.fithealth.R
import com.awesome.fithealth.databinding.FragmentMealDetailBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

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
        fetchMealDetails(mealName.toString())
        Glide.with(requireContext())
            .load(mealImage)
            .into(binding.imageViewMeal)

    }
    private fun fetchMealDetails(mealName: String) {
        GlobalScope.launch {
            try {
                val url = "https://www.themealdb.com/api/json/v1/1/search.php?s=$mealName"
                val response = URL(url).readText()
                val jsonObject = JSONObject(response)
                val mealsArray = jsonObject.optJSONArray("meals")
                mealsArray?.let {
                    for (i in 0 until it.length()) {
                        val mealObject = it.getJSONObject(i)
                        val ingredients = extractIngredients(mealObject)
                        val measures = extractMeasures(mealObject)

                        // Combine ingredients, measures, and quantities
                        val formattedMealDetails = formatMealDetails(ingredients, measures)

                        // Update UI on the main thread
                        launch(Dispatchers.Main) {
                            binding.textViewIngredients.text = "Ingredients:\n$formattedMealDetails"
                        }
                    }
                } ?: println("Meal not found")
            } catch (e: Exception) {
                // Handle errors
                e.printStackTrace()
            }
        }
    }

    private fun formatMealDetails(ingredients: List<String>, measures: List<String>): String {
        val builder = StringBuilder()
        for (i in ingredients.indices) {
            builder.append("${ingredients[i]} - ${measures.getOrElse(i) { "" }}\n")
        }
        return builder.toString()
    }


    private fun extractIngredients(mealObject: JSONObject): List<String> {
        val ingredients = mutableListOf<String>()
        for (i in 1..20) { // Assume maximum 20 ingredients per meal
            val ingredient = mealObject.optString("strIngredient$i")
            if (ingredient.isNotEmpty()) {
                ingredients.add(ingredient)
            } else {
                break // No more ingredients, exit loop
            }
        }
        return ingredients
    }

    private fun extractMeasures(mealObject: JSONObject): List<String> {
        val measures = mutableListOf<String>()
        for (i in 1..20) { // Assume maximum 20 measures per meal
            val measure = mealObject.optString("strMeasure$i")
            if (measure.isNotEmpty()) {
                measures.add(measure)
            } else {
                break // No more measures, exit loop
            }
        }
        return measures
    }
}
