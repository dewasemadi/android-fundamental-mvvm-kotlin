package com.dicoding.githubuser

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.ListUserAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.model.User
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList
import com.dicoding.githubuser.helper.capitalized

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.app_title)

        binding.rvUsers.setHasFixedSize(true)

        list.addAll(getUser(this))
        showRecyclerList()

        Log.d("List", list.toString())
    }

    private fun getUser(context: Context): ArrayList<User> {
        val listUser = ArrayList<User>()

        try {
            val data = context.assets.open("githubuser.json").bufferedReader().use { it.readText() }
            val users = JSONObject(data).getJSONArray("users")
            val resources: Resources = context.resources

            for (i in 0 until users.length()) {
                val obj = users.getJSONObject(i)
                val avatar = obj.getString("avatar")
                val user = User(
                    obj.getString("username"),
                    obj.getString("name").capitalized(),
                    resources.getIdentifier(avatar, "drawable", context.packageName),
                    obj.getString("company"),
                    obj.getString("location"),
                    obj.getInt("repository"),
                    obj.getInt("follower"),
                    obj.getInt("following"),
                )
                listUser.add(user)
            }

        } catch (error: IOException) {
            Log.d("JSON Error", error.toString())
        }
        return listUser
    }

    private fun showRecyclerList() {
        binding.rvUsers.layoutManager = when (applicationContext.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> GridLayoutManager(
                this,
                2
            )
            else -> LinearLayoutManager(this)
        }
        val listUserAdapter = ListUserAdapter(list)
        binding.rvUsers.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showDetailUser(data)
            }
        })
    }

    private fun showDetailUser(data: User) {
        val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        intentToDetail.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(intentToDetail)
    }
}