package com.demo.maps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.maps.data.Repository
import com.demo.maps.data.SearchResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MainViewModel(private val repo: Repository) : ViewModel() {
    private val _query = MutableStateFlow("")
    private val _results = MutableStateFlow<List<SearchResult>>(emptyList())
    val results: StateFlow<List<SearchResult>> = _results.asStateFlow()
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        _query
            .debounce(1000L)
            .filter { it.trim().isNotEmpty() }
            .distinctUntilChanged()
            .onEach { q ->
                search(q)
            }
            .launchIn(viewModelScope)
    }

    fun setQuery(q: String) {
        _query.value = q
        if (q.isBlank()) {
            _results.value = emptyList()
        }
    }

    private fun search(q: String) {
        viewModelScope.launch {

            val items = repo.searchAddress(q)
            _results.value = items
            Log.d("MainViewModel", "Query: $q, Results: $items")
        }
    }

}
