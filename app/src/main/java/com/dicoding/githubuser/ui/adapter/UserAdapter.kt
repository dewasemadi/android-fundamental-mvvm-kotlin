package com.dicoding.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuser.data.local.entity.UserEntity
import com.dicoding.githubuser.databinding.LayoutUserItemBinding
import com.dicoding.githubuser.data.remote.response.User
import com.dicoding.githubuser.utils.setImage

class UserAdapter(
    private val listUser: List<User>,
    private val isFavoritesActivity: Boolean = false,
    private val onDeleteClicked: (UserEntity) -> Unit = {}
) :
    RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(var binding: LayoutUserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = LayoutUserItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, avatarUrl, name) = listUser[position]
        val userEntity = username?.let { UserEntity(it, avatarUrl, name) }

        holder.binding.apply {
            imgItemAvatar.setImage(holder.itemView.context, avatarUrl)
            tvItemUsername.text = if (name.isNullOrEmpty()) username else name
            tvItemUsernameSmall.text = username
            btnDelete.visibility = if (isFavoritesActivity) View.VISIBLE else View.GONE
            btnDelete.setOnClickListener {
                if (userEntity != null) {
                    onDeleteClicked(userEntity)
                }
            }
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount() = listUser.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}