package com.dicoding.githubuser.ui.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.databinding.FragmentFollowingBinding
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.ui.adapter.UserAdapter
import com.dicoding.githubuser.ui.viewmodel.FollowingViewModel
import com.dicoding.githubuser.ui.viewmodel.ViewModelFactory
import com.dicoding.githubuser.utils.ResultResponse

class FollowingFragment : BaseDetailFragment<FragmentFollowingBinding>(){

    override fun inflateLayout(layoutInflater: LayoutInflater) = FragmentFollowingBinding.inflate(layoutInflater)

    private val followingViewModel: FollowingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun initRecycleView(){
        binding.rvFollowing.apply {
            layoutManager = LinearLayoutManager(this.context)
            addItemDecoration(
                DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    override fun initObservers(username: String?) {
        followingViewModel.getUserFollowing(username ?: "").observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultResponse.Loading -> {
                    binding.progressBarFollowing.visibility = View.VISIBLE
                }
                is ResultResponse.Success -> {
                    binding.progressBarFollowing.visibility = View.GONE
                    result.data.let {
                        setUserFollowing(it)
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

    private fun setUserFollowing(users: List<User>) {
        val listFollowingAdapter = UserAdapter(users)
        binding.rvFollowing.adapter = listFollowingAdapter

        listFollowingAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailUser(data)
            }
        })
    }

    private fun isEmpty(users: List<User>) {
        binding.tvFollowingEmpty.visibility = if (users.isNotEmpty()) View.GONE else View.VISIBLE
    }
}