package com.devanasmohammed.search.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.devanasmohammed.search.R
import com.devanasmohammed.search.data.model.Resource
import com.devanasmohammed.search.data.repository.ProductsRepository
import com.devanasmohammed.search.databinding.FragmentSearchBinding
import com.devanasmohammed.search.util.AlertDialogHandler
import com.devanasmohammed.search.util.Constants.Companion.SEARCH_DELAY
import com.devanasmohammed.search.util.ProgressBarHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel

    private lateinit var adapter: ProductsAdapter
    private lateinit var progressBar: ProgressBarHandler

    private var whiteColor = 0
    private var orangeColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initColors()
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_search, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        progressBar = ProgressBarHandler(requireActivity(), binding.root.id)

        viewModel.isFoodItemSelected.observe(viewLifecycleOwner) {
            setSwitchStyle(it)
        }

        viewModel.products.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    progressBar.show()
                }
                is Resource.Success -> {
                    progressBar.hide()
                    adapter.differ.submitList(it.data!!.products)
                }
                is Resource.Error -> {
                    progressBar.hide()
                    AlertDialogHandler().showDialog(
                        requireActivity(),"Uh oh!",
                        "Failed to get products, Please check your internet connection and try again",
                        true
                    ){
                        viewModel.getAllRemoteProducts()
                    }
                    Log.e("SearchFragment", "Error")
                }
            }
        }

        binding.apply {
            var job: Job? = null
            searchEt.addTextChangedListener { editable ->
                job?.cancel()
                job = MainScope().launch {
                    delay(SEARCH_DELAY)
                    editable.let {
                        if (editable.toString().trim().isNotEmpty()) {
                            viewModel.search(editable.toString())
                        } else {
                            adapter.differ.submitList(viewModel.allProducts)
                        }
                    }
                }
            }
            foodItemTv.setOnClickListener {
                toggleBetweenFoodAndRestaurant(viewModel.isFoodItemSelected.value!!, it)
            }

            restaurantTv.setOnClickListener {
                toggleBetweenFoodAndRestaurant(viewModel.isFoodItemSelected.value!!, it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        adapter = ProductsAdapter(requireContext())
        binding.productsRv.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

        binding.productsRv.layoutManager = layoutManager

    }

    private fun initViewModel() {
        val factory = SearchViewModelFactory(ProductsRepository())
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    private fun initColors() {
        orangeColor = ResourcesCompat.getColor(resources, R.color.orange, null)
        whiteColor = ResourcesCompat.getColor(resources, R.color.white, null)
    }

    private fun setSwitchStyle(isFoodItemSelected: Boolean) {

        if (isFoodItemSelected) {
            binding.foodItemTv.setTextColor(whiteColor)
            binding.restaurantTv.setTextColor(orangeColor)

            binding.foodItemTv.setBackgroundResource(R.drawable.bg_switch_selected)
            binding.restaurantTv.setBackgroundResource(R.drawable.bg_switch_unselected)
        } else {
            binding.foodItemTv.setTextColor(orangeColor)
            binding.restaurantTv.setTextColor(whiteColor)

            binding.foodItemTv.setBackgroundResource(R.drawable.bg_switch_unselected)
            binding.restaurantTv.setBackgroundResource(R.drawable.bg_switch_selected)
        }
    }

    private fun toggleBetweenFoodAndRestaurant(isFoodItemSelected: Boolean, view: View) {
        val orangeColor = ResourcesCompat.getColor(resources, R.color.orange, null)
        val whiteColor = ResourcesCompat.getColor(resources, R.color.white, null)

        //if food selected and press restaurant
        if (isFoodItemSelected && view.id == R.id.restaurant_tv) {

            binding.restaurantTv.setBackgroundResource(R.drawable.bg_switch_selected)
            binding.restaurantTv.setTextColor(whiteColor)

            binding.foodItemTv.setTextColor(orangeColor)
            binding.foodItemTv.setBackgroundResource(R.drawable.bg_switch_unselected)

            leftToRightAnimationWithAlpha(binding.restaurantTv)

            viewModel.setIsFoodItemSelected(false)
        }
        //if restaurant selected and press food
        if (!isFoodItemSelected && view.id == R.id.food_item_tv) {

            binding.foodItemTv.setBackgroundResource(R.drawable.bg_switch_selected)
            binding.foodItemTv.setTextColor(whiteColor)

            binding.restaurantTv.setTextColor(orangeColor)
            binding.restaurantTv.setBackgroundResource(R.drawable.bg_switch_unselected)

            rightToLeftAnimationWithAlpha(binding.foodItemTv)
            viewModel.setIsFoodItemSelected(true)
        }
    }

    private fun leftToRightAnimationWithAlpha(view: View) {
        view.animate().alpha(1f).duration = 600
        view.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_in_left,
            ),
        )
    }

    private fun rightToLeftAnimationWithAlpha(view: View) {
        view.animate().alpha(1f).duration = 600
        view.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_in_right,
            ),
        )
    }

}