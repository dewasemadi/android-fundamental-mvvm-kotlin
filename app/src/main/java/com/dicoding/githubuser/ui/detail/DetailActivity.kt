package com.dicoding.githubuser.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.ui.adapter.SectionsPagerAdapter
import com.dicoding.githubuser.util.prettyCount
import com.dicoding.githubuser.util.setImage
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra(EXTRA_USERNAME)
        initViewBinding()
        initViewPager(username)
        initToolbar(username)
        getUserDetail(username)
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

    private fun getUserDetail(username: String?) {
        username?.let { detailViewModel.detailUser(it) }
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
        detailViewModel.detailUser.observe(this) { user ->
            user?.let { setUserDetailData(it) }
            Log.d(TAG, user.toString())
        }
        detailViewModel.isLoading.observe(this) { showLoading(it) }
        detailViewModel.isNetworkError.observe(this) {
            it.getContentIfNotHandled()?.let { isNetworkError ->
                showNetworkError(isNetworkError, username)
            }
        }
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

    private fun hideContent(isVisible: Boolean) {
        binding.detailLayout.root.visibility = if (!isVisible) View.VISIBLE else View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.detailProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        hideContent(isLoading)
    }

    private fun isAttributeEmpty(attribute: String?, txt: TextView) {
        if (attribute.isNullOrBlank())
            txt.text = getString(R.string.empty_attribute)
        else
            txt.text = attribute
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showNetworkError(isNetworkError: Boolean, username: String?) {
        if (isNetworkError)
            hideContent(isNetworkError)

        binding.networkErrorContainer.apply {
            root.visibility = if (isNetworkError) View.VISIBLE else View.GONE
            tvNetworkError.text = getString(R.string.network_error, username)
        }
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