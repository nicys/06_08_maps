package ru.netology.nmedia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.MarkerCardBinding
import ru.netology.nmedia.ui.dto.Marker

interface MarkerOnInteractionListener {
    fun onShowMarker(marker: Marker)
    fun onRemove(marker: Marker)
    fun onEdit(marker: Marker)
}

class MarkersAdapter(
    private val OnInteractionListener: MarkerOnInteractionListener,
) : ListAdapter<Marker, MarkersAdapter.MarkerViewHolder>(MarkerDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerViewHolder {
        val binding =
            MarkerCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MarkerViewHolder(binding, OnInteractionListener)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class MarkerViewHolder(
        private val binding: MarkerCardBinding,
        private val onInteractionListener: MarkerOnInteractionListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(marker: Marker) {
            binding.apply {
                title.text = marker.title
                latitudeText.text = marker.latitude.toString()
                longitudeText.text = marker.longitude.toString()

                card.setOnClickListener {
                    onInteractionListener.onShowMarker(marker)
                }

                popupMenu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.marker_options)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
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

                binding.root.setOnClickListener {
                    onInteractionListener.onShowMarker(marker)
                }

            }
        }
    }

    class MarkerDiffCallBack : DiffUtil.ItemCallback<Marker>() {
        override fun areItemsTheSame(oldItem: Marker, newItem: Marker): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Marker, newItem: Marker): Boolean {
            return oldItem == newItem
        }
    }
}