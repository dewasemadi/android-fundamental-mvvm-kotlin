package com.dicoding.githubuser.ui.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.viewmodel.MainViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory
import com.dicoding.githubuser.utils.ResultResponse

class MainActivity : AppCompatActivity() {
    private var search: String? = null

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        initToolbar()
        initRecycleView()
        observeThemeSetting()
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
                        initObservers(query)
                        searchView.clearFocus()
                    }
                    return true
                }

                override fun onQueryTextChange(query: String) = false
            })
        }
        return true
    }

    private fun initObservers(query: String) {
        mainViewModel.getUsersByUsername(query).observe(this) { result ->
            when (result) {
                is ResultResponse.Loading -> {
                    binding.apply {
                        emptyStateContainer.root.visibility = View.GONE
                        notFoundContainer.root.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        rvUsers.visibility = View.GONE
                    }
                }
                is ResultResponse.Success -> {
                    binding.apply {
                        progressBar.visibility = View.GONE
                        rvUsers.visibility = View.VISIBLE
                    }
                    result.data.items?.let {
                        setUserData(it)
                        Log.d(TAG, it.toString())
                    }
                }
                is ResultResponse.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error Occurred ${result.error}", Toast.LENGTH_SHORT).show()
                }
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
                binding.apply {
                    notFoundContainer.root.visibility = View.GONE
                    emptyStateContainer.root.visibility = View.GONE
                }
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

    private fun isUserPlural(count: Int) = if (count > 1) "users" else "user"

    private fun showDetailUser(data: User) {
        val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
        startActivity(intentToDetail)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorites -> {
                val toFavorites = Intent(this, FavoritesActivity::class.java)
                startActivity(toFavorites)
                true
            }
            R.id.settings -> {
                val toSettings = Intent(this, SettingsActivity::class.java)
                startActivity(toSettings)
                true
            }
            else -> true
        }
    }

    private fun observeThemeSetting(){
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            val default = if(isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            AppCompatDelegate.setDefaultNightMode(default)
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
