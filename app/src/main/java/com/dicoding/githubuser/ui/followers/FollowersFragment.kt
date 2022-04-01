package com.dicoding.githubuser.ui.followers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowersBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private val followersViewModel by viewModels<FollowersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(EXTRA_USERNAME)
        initRecycleView()
        getFollowers(username)
        initObservers()
    }

    private fun initRecycleView() {
        binding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun initObservers() {
        followersViewModel.listFollowers.observe(viewLifecycleOwner) { users ->
            users?.let {
                setUserFollowers(it)
                isEmpty(users)
            }
            Log.d(TAG, users.toString())
        }
        followersViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        followersViewModel.isNetworkError.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isNetworkError ->
                showNetworkError(isNetworkError)
            }
        }
    }

    private fun isEmpty(users: List<User>) {
        binding.tvFollowersEmpty.visibility = if (users.isNotEmpty()) View.GONE else View.VISIBLE
    }

    private fun getFollowers(username: String?) {
        username?.let { followersViewModel.getUserFollowers(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFollowers.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserFollowers(users: List<User>) {
        val listFollowersAdapter = UserAdapter(users)
        binding.rvFollowers.adapter = listFollowersAdapter

        listFollowersAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailUser(data)
            }
        })
    }

    private fun showDetailUser(data: User) {
        val intentToDetail = Intent(activity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
        startActivity(intentToDetail)
    }

    private fun showNetworkError(isNetworkError: Boolean) {
        if (isNetworkError) {
            activity?.let {
                Snackbar.make(
                    it.findViewById(android.R.id.content),
                    "Network Error",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val TAG = "FollowersFragment"
        const val EXTRA_USERNAME = "extra_username"
    }
}