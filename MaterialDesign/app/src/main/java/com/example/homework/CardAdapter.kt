package com.example.homework


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.homework.databinding.CardBinding
import com.google.android.material.card.MaterialCardView

class CardAdapter(val onClick: (view: MaterialCardView, id: Long) -> Unit) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var objects = listOf<CardObject>()


    class CardViewHolder(view: View, onClick: (view: MaterialCardView, position: Int) -> Unit) : RecyclerView.ViewHolder(view) {
        private val viewBinding: CardBinding by viewBinding()

        init {
            itemView.setOnClickListener {
                onClick(viewBinding.root, adapterPosition)
            }
        }

        fun bind(card: CardObject) {
            viewBinding.root.transitionName = card.id.toString()
            viewBinding.titleTextView.text = card.title
            viewBinding.secondaryTextView.text = card.price
            Glide.with(viewBinding.root)
                    .load(card.image)
                    .into(viewBinding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)) { view, position ->
            onClick(view, objects[position].id)
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(objects[position])
    }

    override fun getItemCount(): Int {
        return objects.size
    }

    fun updateList(cards: List<CardObject>) {
        objects = cards
        notifyDataSetChanged()
    }
}