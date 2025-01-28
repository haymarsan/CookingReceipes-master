package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.adapter.BlogspotAdapter
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.databinding.ActivityHomeBinding
import com.hms.cookingreceipes.viewmodel.CookingAppViewModel
import com.hms.cookingreceipes.viewmodel.HomeUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private val mAppViewModel: CookingAppViewModel by viewModels<CookingAppViewModel>()
    private lateinit var blogspotAdapter: BlogspotAdapter
    private lateinit var binding: ActivityHomeBinding
    private val limit = 3

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        blogspotAdapter = BlogspotAdapter()
        binding.contentHome.recyclerMain.layoutManager = LinearLayoutManager(this)
        binding.contentHome.recyclerMain.recycledViewPool.setMaxRecycledViews(0, 0)
        binding.contentHome.recyclerMain.adapter = blogspotAdapter

        lifecycleScope.launch {
            mAppViewModel.homeUiState.collectLatest {
                when (it) {
                    is HomeUiState.Loading -> {
                        binding.mainProgress.visibility = View.VISIBLE
                    }

                    is HomeUiState.Success -> {
                        binding.mainProgress.visibility = View.GONE
                        blogspotAdapter.entryList = it.feeds
                    }

                    is HomeUiState.Error -> {
                        binding.mainProgress.visibility = View.GONE
                        showToast(it.message)
                    }
                }

            }
        }

        mAppViewModel.loadMoreItems(limit)
        binding.contentHome.recyclerMain.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItemPosition + visibleItemCount >= totalItemCount)
                    mAppViewModel.loadMoreItems(limit)
            }
        })

        blogspotAdapter.setOnItemClickListener(object : BlogspotAdapter.OnItemClickListener {
            override fun onItemClick(entry: Entry) {
                startActivity(WebPageActivity.newInstance(this@HomeActivity, entry))
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_update -> openMarket(
                this,
                getString(R.string.app_url)
            )
        }
        return true
    }
}
