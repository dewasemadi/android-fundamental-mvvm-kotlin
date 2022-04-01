package com.dicoding.githubuser.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.databinding.FragmentFollowersBinding
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.viewmodel.FollowersViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory
import com.dicoding.githubuser.utils.ResultResponse

class FollowersFragment : BaseDetailFragment<FragmentFollowersBinding>() {

    override fun inflateLayout(layoutInflater: LayoutInflater) = FragmentFollowersBinding.inflate(layoutInflater)

    private val followersViewModel: FollowersViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun initRecycleView(){
        binding.rvFollowers.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    override fun initObservers(username: String?) {
        followersViewModel.getUserFollowers(username ?: "").observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultResponse.Loading -> {
                    binding.progressBarFollowers.visibility = View.VISIBLE
                }
                is ResultResponse.Success -> {
                    binding.progressBarFollowers.visibility = View.GONE
                    result.data.let {
                        setUserFollowers(it)
                        isEmpty(it)
                        Log.d(TAG, it.toString())
                    }
                }
                is ResultResponse.Error -> {
                    Toast.makeText(requireContext(), "Error Occurred ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun isEmpty(users: List<User>) {
        binding.tvFollowersEmpty.visibility = if (users.isNotEmpty()) View.GONE else View.VISIBLE
    }
}