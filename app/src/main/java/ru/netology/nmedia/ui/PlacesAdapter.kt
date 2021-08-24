package ru.netology.nmedia.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPlaceBinding
import ru.netology.nmedia.ui.dto.Place

class PlacesAdapter(
    private val placeClickListener: (name: Place) -> Unit,
) : ListAdapter<Place, PlacesViewHolder>(
    object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
            oldItem == newItem
    }
) {
    override fun getItemViewType(position: Int): Int = R.layout.card_place

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder =
        PlacesViewHolder(
            CardPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ).apply {
            itemView.setOnClickListener {
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.25F, 1F)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.25F, 1F)
                val animator: ObjectAnimator =
                    ObjectAnimator.ofPropertyValuesHolder(it, scaleX, scaleY).apply {
                        duration = 500
                        repeatCount = 1
                        interpolator = BounceInterpolator()
                    }.apply { start() }
                it.tag = animator
                placeClickListener(getItem(bindingAdapterPosition))
            }
        }.also {
            println("on create viewHolder")
        }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        (holder.itemView.tag as? Animator)?.cancel()
        holder.binding.markerName.text = getItem(position).name
        println("on bind viewHolder")
    }

//    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
//        val place = getItem(position)
//        holder.bind(place)
//    }
}

class PlacesViewHolder(val binding: CardPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Place) {
        binding.apply {
            markerName.text = place.name
        }
    }
}