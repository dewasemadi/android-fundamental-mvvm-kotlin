package com.dicoding.githubuser.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.dicoding.githubuser.data.remote.response.User

// this abstract class is parent of FollowersFragment and FollowingFragment
abstract class BaseDetailFragment<T: ViewBinding>: Fragment() {

    // avoid memory leaks
    private var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflateLayout(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(EXTRA_USERNAME)
        initRecycleView()
        initObservers(username)
    }

    abstract fun inflateLayout(layoutInflater: LayoutInflater): T

    abstract fun initRecycleView()

    abstract fun initObservers(username: String?)

    fun showDetailUser(data: User) {
        val intentToDetail = Intent(activity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.username)
        startActivity(intentToDetail)
    }

    companion object {
        const val TAG = "BaseDetailFragment"
        const val EXTRA_USERNAME = "extra_username"
    }
}