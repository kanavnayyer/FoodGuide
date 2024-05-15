package com.awesome.fithealth.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.awesome.fithealth.Adapter.MyAdapter
import com.awesome.fithealth.R
import com.awesome.fithealth.ViewModel.ViewModel
import com.awesome.fithealth.databinding.FragmentMealScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class MealScreenFragment : Fragment(), MyAdapter.OnItemClickListener {

    private lateinit var binding: FragmentMealScreenBinding
    private val mealList = ArrayList<ViewModel>()
    private lateinit var mealAdapter: MyAdapter // Remove the '?' to avoid nullable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMealScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealAdapter = MyAdapter(mealList, this) // Initialize without '?'
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = mealAdapter
        fetchMealData()
    }

    private fun fetchMealData() {
        GlobalScope.launch {
            val alphabet = ('a'..'z').toList()
            val deferredList = alphabet.map { letter ->
                async(Dispatchers.IO) {
                    val url = "https://www.themealdb.com/api/json/v1/1/search.php?f=$letter"
                    val response = URL(url).readText()
                    val jsonObject = JSONObject(response)
                    val mealsArray = jsonObject.optJSONArray("meals")
                    mealsArray?.let {
                        for (i in 0 until it.length()) {
                            val mealObject = it.getJSONObject(i)
                            val mealName = mealObject.getString("strMeal")
                            val mealDescription = mealObject.getString("strInstructions")
                            val mealImage = mealObject.getString("strMealThumb")
                            mealList.add(ViewModel(mealName, mealDescription, mealImage))
                        }
                    }
                }
            }

            deferredList.awaitAll()

            withContext(Dispatchers.Main) {
                mealAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(position: Int, item: ViewModel) {
        val fragment = MealDetailFragment()
        val bundle = Bundle().apply {
            putString("mealName", item.name)
            putString("mealDescription", item.desc)
            putString("mealImage", item.immgu)
        }
        fragment.arguments = bundle

        // Replace the current fragment with MealDetailFragment
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()



    }}
