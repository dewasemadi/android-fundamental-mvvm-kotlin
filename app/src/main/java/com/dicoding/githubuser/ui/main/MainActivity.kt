package com.dicoding.githubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private var search: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        initToolbar()
        initRecycleView()
        initObservers()
    }

    private fun initViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initToolbar() {
        supportActionBar?.title = getString(R.string.app_title)
        binding.rvUsers.setHasFixedSize(true)
    }

    private fun initRecycleView() {
        binding.rvUsers.apply {
            layoutManager = when (applicationContext.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this@MainActivity, 2)
                else -> LinearLayoutManager(this@MainActivity)
            }
            addItemDecoration(
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun initObservers() {
        mainViewModel.listUser.observe(this) { users ->
            users?.let { setUserData(it) }
            Log.d(TAG, users.toString())
        }
        mainViewModel.isLoading.observe(this) { showLoading(it) }
        mainViewModel.isNetworkError.observe(this) {
            it.getContentIfNotHandled()?.let { isNetworkError ->
                showNetworkError(isNetworkError)
            }
        }
    }

    private fun setUserData(users: List<User>) {
        val listUserAdapter = UserAdapter(users)
        binding.rvUsers.adapter = listUserAdapter
        val count = listUserAdapter.itemCount
        val userTxt = isUserPlural(count)

        when (count) {
            in 1..count -> {
                binding.notFoundContainer.root.visibility = View.GONE
                binding.emptyStateContainer.root.visibility = View.GONE
                Toast.makeText(this, "Found $count $userTxt", Toast.LENGTH_SHORT).show()
            }
            else -> {
                binding.notFoundContainer.apply {
                    root.visibility = View.VISIBLE
                    tvNotFound.text = getString(R.string.not_found_txt, search)
                }
            }
        }

        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailUser(data)
            }
        })
    }

    private fun isUserPlural(count: Int): String {
        if (count == 1)
            return "user"
        else if (count > 1)
            return "users"
        return ""
    }

    private fun showDetailUser(data: User) {
        val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
        startActivity(intentToDetail)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.apply {
            maxWidth = Integer.MAX_VALUE
            queryHint = getString(R.string.search_hint)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        search = query
                        mainViewModel.getUsersByUsername(query)
                        searchView.clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(query: String) = false
            })
        }
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            binding.emptyStateContainer.root.visibility = View.GONE
            binding.notFoundContainer.root.visibility = View.GONE
        }

        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            rvUsers.visibility = if (!isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showNetworkError(isNetworkError: Boolean) {
        if (isNetworkError)
            Snackbar.make(window.decorView.rootView, "Network Error", Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}