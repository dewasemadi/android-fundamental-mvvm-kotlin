package com.dicoding.githubuser.ui.following

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
import com.dicoding.githubuser.databinding.FragmentFollowingBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private val followingViewModel by viewModels<FollowingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(EXTRA_USERNAME)
        initRecycleView()
        getFollowing(username)
        initObservers()
    }

    private fun initRecycleView() {
        binding.rvFollowing.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun initObservers() {
        followingViewModel.listFollowing.observe(viewLifecycleOwner) { users ->
            users?.let {
                setUserFollowing(it)
                isEmpty(users)
            }
            Log.d(TAG, users.toString())
        }
        followingViewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        followingViewModel.isNetworkError.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { isNetworkError ->
                showNetworkError(isNetworkError)
            }
        }
    }

    private fun isEmpty(users: List<User>) {
        binding.tvFollowingEmpty.visibility = if (users.isNotEmpty()) View.GONE else View.VISIBLE
    }

    private fun getFollowing(username: String?) {
        username?.let { followingViewModel.getUserFollowing(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBarFollowing.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setUserFollowing(users: List<User>) {
        val listFollowersAdapter = UserAdapter(users)
        binding.rvFollowing.adapter = listFollowersAdapter

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
        const val TAG = "FollowingFragment"
        const val EXTRA_USERNAME = "extra_username"
    }
}