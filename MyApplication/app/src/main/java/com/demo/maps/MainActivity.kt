package com.demo.maps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.demo.maps.adapters.Adapters
import com.demo.maps.api.Services
import com.demo.maps.data.Repository
import com.demo.maps.databinding.ActivityMainBinding
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: Adapters

    private val locIqService by lazy { Services.create() }
    private val repo by lazy {
        Repository(locIqService, BuildConfig.LOCATIONIQ_KEY)
    }

    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repo) as T
        }
    }

    private val viewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = Adapters(this) { item ->
            openInGoogleMaps(item.lat, item.lng, item.title)
        }
        binding.searchResults.adapter = adapter
        binding.searchResults.layoutManager =
            LinearLayoutManager(this)

        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val q = s?.toString() ?: ""
                viewModel.setQuery(q)
                adapter.highlightKeyword = q
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        lifecycleScope.launchWhenStarted {
            viewModel.results.collect { list ->
                adapter.submit(list)
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun openInGoogleMaps(lat: Double?, lng: Double?, label: String) {
        if (lat == null || lng == null) return
        val uri = "google.navigation:q=$lat,$lng".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            val guri = "https://www.google.com/maps/dir/?api=1&destination=$lat,$lng".toUri()
            startActivity(Intent(Intent.ACTION_VIEW, guri))
        }
    }
}
