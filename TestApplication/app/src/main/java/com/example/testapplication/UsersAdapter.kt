package com.example.testapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.testapplication.databinding.UserItemBinding

class UsersAdapter(private val context: Context, private val onClick: (user: Person) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    private var userList = listOf<Person>()

    class UserViewHolder(
        private val context: Context,
        view: View,
        onClick: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val binding: UserItemBinding by viewBinding()

        init {
            binding.userName.setOnClickListener {
                onClick(adapterPosition)
            }
        }


        fun bind(user: Person) {
            binding.userName.text =
                context.getString(R.string.user_name, user.last_name, user.first_name)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            context,
            LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        ) { position ->
            onClick(userList[position])
        }

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size

    }

    fun updateList(list: List<Person>) {
        userList = list
        notifyDataSetChanged()
    }
}