package com.skillbox.github

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.repo_item.*

class ReposListAdapter(val onItemClick: (id: Long, name: String, owner: String) -> Unit) :
    RecyclerView.Adapter<ReposListAdapter.RepoViewHolder>() {

    private var repos = listOf<Repository>()

    class RepoViewHolder(override val containerView: View, onClick: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        init {

            containerView.setOnClickListener { onClick(adapterPosition) }
        }

        fun bind(repo: Repository) {
            nameTextView.text = repo.name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repo_item, parent, false)
        ) {
            val repo = repos[it]
            onItemClick(repo.id, repo.name, repo.owner.login)

        }
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(repos[position])

    }

    override fun getItemCount(): Int {
        return repos.size

    }


    fun updateList(newList: List<Repository>) {
        repos = newList
        notifyDataSetChanged()
    }
}