package com.devanasmohammed.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devanasmohammed.search.data.repository.ProductsRepository

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory
    (private val productsRepository: ProductsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(productsRepository) as T
    }
}