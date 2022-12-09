package com.devanasmohammed.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devanasmohammed.search.data.model.Product
import com.devanasmohammed.search.data.model.ProductsResponse
import com.devanasmohammed.search.data.model.Resource
import com.devanasmohammed.search.data.repository.ProductsRepository
import com.devanasmohammed.search.util.Constants.Companion.SEARCH_DELAY
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(
    private val productsRepository: ProductsRepository
) : ViewModel() {

    val allProducts = mutableListOf<Product>()

    private val _products: MutableLiveData<Resource<ProductsResponse>> = MutableLiveData()
    val products: LiveData<Resource<ProductsResponse>> get() = _products

    private val _isFoodItemSelected: MutableLiveData<Boolean> = MutableLiveData(true)
    val isFoodItemSelected: LiveData<Boolean> get() = _isFoodItemSelected

    init {
        getAllRemoteProducts()
    }

    private fun getAllRemoteProducts() {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            _products.postValue(
                handleBreakingNewsResponse(
                    productsRepository.getRemoteProducts()
                )
            )
        }
    }

    fun setIsFoodItemSelected(isSelected: Boolean) {
        _isFoodItemSelected.postValue(isSelected)
    }

    private fun handleBreakingNewsResponse(response: Response<ProductsResponse>?):
            Resource<ProductsResponse> {
        if (response != null && response.isSuccessful) {
            val productList = mutableListOf<Product>()
            //add empty product for results
            response.body().let {
                productList.add(Product(it!!.products.size.toString()))
                productList.addAll(it.products)
                allProducts.addAll(productList)
                return Resource.Success(ProductsResponse(it.limit, productList, it.skip, it.total))
            }
        }
        return Resource.Error("")
    }

    fun search(query: String) {
        viewModelScope.launch {
//            _products.postValue(Resource.Loading())
            //delay for two seconds before search
            _products.postValue(
                handleSearch(query)
            )
        }
    }

    private fun handleSearch(query: String): Resource<ProductsResponse> {
        val resultList = mutableListOf<Product>()
        for (product in allProducts) {
            if (product.title.lowercase().contains(query.lowercase())) {
                resultList.add(product)
            }
        }
        val finalList = mutableListOf<Product>()
        finalList.add(Product(resultList.size.toString()))
        finalList.addAll(resultList)
        val response = ProductsResponse(0, finalList, 0, resultList.size)
        return Resource.Success(response)
    }

}