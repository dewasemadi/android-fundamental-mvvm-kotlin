package com.dicoding.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.ui.view.BaseDetailFragment
import com.dicoding.githubuser.ui.view.FollowersFragment
import com.dicoding.githubuser.ui.view.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String?) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        val bundle = Bundle()
        bundle.putString(BaseDetailFragment.EXTRA_USERNAME, username)

        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }

        fragment?.arguments = bundle
        return fragment as Fragment
    }
}