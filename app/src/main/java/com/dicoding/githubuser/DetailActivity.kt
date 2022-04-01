package com.dicoding.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.githubuser.databinding.ActivityDetailBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.helper.prettyCount
import com.dicoding.githubuser.helper.setImage

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val person = intent.getParcelableExtra<User>(EXTRA_USER) as User

        supportActionBar?.title = person.username.toString()

        binding.apply {
            imgDetailAvatar.setImage(this@DetailActivity, person.avatar)
            tvDetailName.text = person.name.toString()
            tvDetailUsername.text = person.username.toString()
            tvDetailFollower.text = person.follower?.toLong()?.prettyCount()
            tvDetailFollowing.text = person.following?.toLong()?.prettyCount()
            tvDetailRepository.text = person.repository?.toLong()?.prettyCount()
            tvDetailLocation.text = person.location.toString()
            tvDetailCompany.text = person.company.toString()
        }
        Log.d("Detail User", person.name.toString())
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}