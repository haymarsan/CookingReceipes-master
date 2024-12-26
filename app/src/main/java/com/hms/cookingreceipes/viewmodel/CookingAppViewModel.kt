package com.hms.cookingreceipes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hms.cookingreceipes.data.model.Blogspot
import com.hms.cookingreceipes.repository.ReceipesRepository
import com.hms.cookingreceipes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CookingAppViewModel @Inject constructor(private val receipesRepository: ReceipesRepository) :
    ViewModel() {
    private var _blogSpot = MutableLiveData<NetworkResult<Blogspot>>()
    val blogspot: LiveData<NetworkResult<Blogspot>> get() = _blogSpot

    fun getBlogArticles(params: Map<String, String>) {
        _blogSpot.value = NetworkResult.Loading()
        viewModelScope.launch {
            _blogSpot.value = receipesRepository.getBlogSpotArticles(params)
        }
    }
}