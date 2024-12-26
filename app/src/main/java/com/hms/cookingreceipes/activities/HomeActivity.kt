package com.hms.cookingreceipes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hms.cookingreceipes.R
import com.hms.cookingreceipes.adapter.BlogspotAdapter
import com.hms.cookingreceipes.data.model.Entry
import com.hms.cookingreceipes.databinding.ActivityHomeBinding
import com.hms.cookingreceipes.utils.AppUtils
import com.hms.cookingreceipes.utils.NetworkResult
import com.hms.cookingreceipes.viewmodel.CookingAppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {
    private val mAppViewModel: CookingAppViewModel by viewModels<CookingAppViewModel>()
    private lateinit var blogspotAdapter: BlogspotAdapter
    private lateinit var binding: ActivityHomeBinding

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

        mAppViewModel.blogspot.observe(this) {
            when (it) {
                is NetworkResult.Loading -> {
                    binding.mainProgress.visibility = View.VISIBLE
                }

                is NetworkResult.Success -> {
                    binding.mainProgress.visibility = View.GONE
                    it.data?.let {
                        blogspotAdapter.entryList += it.feed.entry
                        binding.mainProgress.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {
                    binding.mainProgress.visibility = View.GONE
                    showToast(it.message ?: "Error in loading error")
                }
            }
        }

        if (AppUtils().hasConnection(this)) {
            binding.mainProgress.visibility = View.VISIBLE
            val params = HashMap<String, String>()
            params["alt"] = "json"
//            params["start-index"] = "1"
//            params["max-results"] = "5"

            mAppViewModel.getBlogArticles(params)
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()
        }
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
