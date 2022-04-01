package com.dicoding.githubuser.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.ui.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.ui.viewmodel.DetailViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory
import com.dicoding.githubuser.utils.ResultResponse
import com.dicoding.githubuser.utils.prettyCount
import com.dicoding.githubuser.utils.setImage
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private var userEntity: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        initViewBinding()
        initViewPager(username)
        initToolbar(username)
        initObservers(username)
    }

    private fun initViewPager(username: String?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        binding.detailLayout.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(
            binding.detailLayout.tabs,
            binding.detailLayout.viewPager
        ) { tab, position ->
            tab.text = getString(TAB_TITLES[position])
        }.attach()
    }

    private fun initViewBinding() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initToolbar(title: String?) {
        supportActionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun initObservers(username: String?) {
        detailViewModel.getUserDetail(username ?: "").observe(this) { result ->
            when (result) {
                is ResultResponse.Loading -> {
                    binding.apply {
                        detailLayout.root.visibility = View.GONE
                        detailProgressBar.visibility = View.VISIBLE
                    }
                }
                is ResultResponse.Success -> {
                    binding.apply {
                        detailLayout.root.visibility = View.VISIBLE
                        detailProgressBar.visibility = View.GONE
                    }
                    result.data.let {
                        setUserDetailData(it)
                        userEntity = it.username?.let { it1 -> UserEntity(it1, it.avatarUrl, it.name) } // global
                        Log.d(TAG, userEntity.toString())
                    }
                }
                is ResultResponse.Error -> {
                    binding.detailLayout.root.visibility = View.GONE
                    binding.networkErrorContainer.apply {
                        root.visibility = View.VISIBLE
                        tvNetworkError.text = getString(R.string.network_error, username)
                    }
                    Toast.makeText(this, "Error Occurred ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        detailViewModel.isFavoriteUser(username ?: "").observe(this) { isFavorite ->
            val icon = if (isFavorite) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24

            binding.detailLayout.favoriteButton.apply {
                setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, icon))

                setOnClickListener {
                    Log.d(TAG, "clicked!")
                    userEntity?.let {
                        if (isFavorite) deleteFromFavorite(it) else addToFavorite(it)
                    }
                }
            }
        }
    }

    private fun addToFavorite(user: UserEntity) {
        detailViewModel.setFavoriteUser(user)
        Toast.makeText(this, "Successfully added ${user.name ?: user.username} as your favorite user", Toast.LENGTH_SHORT).show()
    }

    private fun deleteFromFavorite(user: UserEntity) {
        detailViewModel.deleteFavoriteUser(user)
        Toast.makeText(this, "Successfully removed ${user.name ?: user.username} as your favorite user", Toast.LENGTH_SHORT).show()
    }

    private fun setUserDetailData(user: User) {
        binding.detailLayout.apply {
            isAttributeEmpty(user.name, tvDetailName)
            isAttributeEmpty(user.username, tvDetailUsername)
            isAttributeEmpty(user.company, tvDetailCompany)
            isAttributeEmpty(user.location, tvDetailLocation)
            isAttributeEmpty(user.blog, tvDetailBlog)
            isAttributeEmpty(user.email, tvDetailEmail)
            isAttributeEmpty(user.twitterUsername, tvDetailTwitter)
            imgDetailAvatar.setImage(this@DetailActivity, user.avatarUrl)
            tvDetailBio.text = user.bio
            tvFollowersValue.text = user.followers?.toLong()?.prettyCount()
            tvFollowingValue.text = user.following?.toLong()?.prettyCount()
            tvRepositoryValue.text = user.publicRepos?.toLong()?.prettyCount()
        }
    }

    private fun isAttributeEmpty(attribute: String?, txt: TextView) {
        if (attribute.isNullOrBlank())
            txt.text = getString(R.string.empty_attribute)
        else
            txt.text = attribute
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bookmark_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bookmark -> {
                val toFavorites = Intent(this, FavoritesActivity::class.java)
                startActivity(toFavorites)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val TAG = "DetailActivity"
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}