package com.dicoding.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.githubuser.ui.followers.FollowersFragment
import com.dicoding.githubuser.ui.following.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String?) :
    FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        val mBundle = Bundle()

        when (position) {
            0 -> {
                fragment = FollowersFragment()
                mBundle.putString(FollowersFragment.EXTRA_USERNAME, username)
            }
            1 -> {
                fragment = FollowingFragment()
                mBundle.putString(FollowingFragment.EXTRA_USERNAME, username)
            }
        }

        fragment?.arguments = mBundle
        return fragment as Fragment
    }
}