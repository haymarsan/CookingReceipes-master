package com.hms.cookingreceipes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hms.cookingreceipes.repository.ReceipesRepository
import com.hms.cookingreceipes.utils.Feeds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CookingAppViewModel @Inject constructor(private val receipesRepository: ReceipesRepository) :
    ViewModel() {

    private val _feeds = MutableStateFlow<Feeds>(emptyList())
    private val _homeUiState = MutableStateFlow<HomeUiState>(
        HomeUiState.Loading
    )
    val homeUiState get() = _homeUiState


    private var isLoading = false

    private var startIndex = 1

    fun loadMoreItems(maxItem: Int) {
//        if(isLoading || startIndex > totalItem) return
        if (isLoading) return
        isLoading = true
        _homeUiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val params = HashMap<String, String>()
                params["alt"] = "json"
                params["start-index"] = startIndex.toString()
                params["max-results"] = maxItem.toString()

                val result = receipesRepository.getBlogSpotArticles(params)
                result.onSuccess {
                    _feeds.value += it.feed.entry
                    _homeUiState.value = HomeUiState.Success(_feeds.value)
                    startIndex += 5
                }
            } catch (e: Exception) {
                _homeUiState.value =  HomeUiState.Error(e.localizedMessage?: "Something went wrong")
            } finally {
                isLoading = false
            }
        }
    }
}

sealed interface HomeUiState{
    data object Loading: HomeUiState
    data class Error(val message: String): HomeUiState
    data class Success(val feeds: Feeds): HomeUiState
}

