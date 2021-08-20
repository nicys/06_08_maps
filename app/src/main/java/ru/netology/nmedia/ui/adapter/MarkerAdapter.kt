package ru.netology.nmedia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardMarkerBinding
import ru.netology.nmedia.ui.dto.Marker

interface OnInteractionListener {
    fun onEdit(marker: Marker) {}
    fun onRemove(marker: Marker) {}
}

class MarkerAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Marker, MarkerViewHolder>(MarkerDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding = CardMarkerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarkerViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        val marker = getItem(position)
        holder.bind(marker)
    }
}

class MarkerViewHolder(
    private val binding: CardMarkerBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(marker: Marker) {
        binding.apply {
            markerName.text = marker.title
            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_marker)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.delete -> {
                                onInteractionListener.onRemove(marker)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(marker)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }

}

class MarkerDiffCallback : DiffUtil.ItemCallback<Marker>() {
    override fun areItemsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Marker, newItem: Marker): Boolean {
        return oldItem == newItem
    }
}