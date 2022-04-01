package com.dicoding.githubuser.ui.view

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.databinding.ActivityFavoritesBinding
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.viewmodel.FavoritesViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory
import com.dicoding.githubuser.utils.onAlertDialog

class FavoritesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesBinding

    private val favoritesViewModel: FavoritesViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBinding()
        initToolbar()
        initRecycleView()
        initObservers()
    }

    private fun initViewBinding() {
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initToolbar() {
        supportActionBar?.apply {
            this.title = getString(R.string.favorites)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initRecycleView() {
        binding.rvFavoriteUsers.apply {
            layoutManager = when (applicationContext.resources.configuration.orientation) {
                Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(this@FavoritesActivity, 2)
                else -> LinearLayoutManager(this@FavoritesActivity)
            }
            addItemDecoration(
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun initObservers() {
        favoritesViewModel.getFavoriteUsers().observe(this) { favoriteUsers ->
            showAllFavorites(favoriteUsers)
        }
    }

    private fun showAllFavorites(favoriteUsers: List<UserEntity>) {
        val listUsers = ArrayList<User>()
        for (user in favoriteUsers) {
            val data = User(
                user.username, user.avatarUrl, user.name,
                null, null, null, null, null,
                null, null, null, null, null,
            )
            listUsers.add(data)
        }

        val listFavoriteUsers = UserAdapter(listUsers, true) {
            onAlertDialog(
                this,
                "Delete",
                "Are you sure to delete ${it.name ?: it.username} as your favorite?",
                "Cancel",
                "Delete"
            ) {
                favoritesViewModel.deleteFavoriteUser(it) // callback implementation
                Toast.makeText(
                    this,
                    "Successfully removed ${it.name ?: it.username} from favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        val count = listUsers.size
        binding.apply {
            rvFavoriteUsers.adapter = listFavoriteUsers
            tvEmptyFavorite.visibility = if (count > 0) View.GONE else View.VISIBLE
        }

        listFavoriteUsers.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailUser(data)
            }
        })
    }

    private fun showDetailUser(data: User) {
        val intentToDetail = Intent(this@FavoritesActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
        startActivity(intentToDetail)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all -> {
                onAlertDialog(
                    this,
                    "Delete All Favorites",
                    "Are you sure to delete all your favorites?",
                    "Cancel",
                    "Delete"
                ) {
                    favoritesViewModel.deleteAllFavorite()
                    Toast.makeText(
                        this,
                        "Successfully deleted all favorites users",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}